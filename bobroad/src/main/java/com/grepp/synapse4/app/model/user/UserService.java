package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.VoteService;
import com.grepp.synapse4.app.model.user.dto.request.EditInfoRequest;
import com.grepp.synapse4.app.model.user.dto.request.UserSignUpRequest;
import com.grepp.synapse4.app.model.user.dto.response.FindIdResponseDto;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.grepp.synapse4.app.model.auth.code.Role.ROLE_ADMIN;
import static com.grepp.synapse4.app.model.auth.code.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final MailService mailService;
    private final MeetingService meetingService;
    private final VoteService voteService;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void signup(UserSignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = request.toEntity(encodedPassword, ROLE_USER);
        userRepository.save(user);
    }

    public void validateDuplicateUser(UserSignUpRequest request, BindingResult bindingResult) {
        userValidator.validateIdentifiers(
                request.getUserAccount(),
                request.getEmail(),
                request.getNickname(),
                null,
                bindingResult
        );
    }

    public void validateEditUser(EditInfoRequest request, User user, BindingResult bindingResult) {
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            bindingResult.rejectValue("currentPassword", "invalid", "기존 비밀번호가 일치하지 않습니다.");
        }

        userValidator.validateIdentifiers(
                null,
                request.getEmail(),
                request.getNickname(),
                user.getId(),
                bindingResult
        );

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            String pw = request.getNewPassword();

            // ash : 정규표현식(\\S warning 처리)
            if (!pw.matches("^(?=.*[A-Za-z])(?=.*\\d)\\S{4,16}$")) {
                bindingResult.rejectValue("newPassword", "pattern", "비밀번호는 공백 없이 영문자와 숫자를 포함한 4~16자여야 합니다.");
            }

            if (passwordEncoder.matches(pw, user.getPassword())) {
                bindingResult.rejectValue("newPassword", "sameAsOld", "기존 비밀번호와 동일합니다.");
            }

            if (!pw.equals(request.getConfirmNewPassword())) {
                bindingResult.rejectValue("confirmNewPassword", "mismatch", "새 비밀번호가 일치하지 않습니다.");
            }
        }
    }

    public void updateUserInfo(EditInfoRequest request, User user) {
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());

        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        userRepository.save(user);
    }

    public void signupAdmin(UserSignUpRequest request) {
        User user = buildUser(request);
        user.setRole(ROLE_ADMIN);
        user.setIsSurvey(false);
        user.setActivated(true);
        user.setDeletedAt(null);
        userRepository.save(user);
    }

    private User buildUser(UserSignUpRequest req) {
        return User.builder()
                .userAccount(req.getUserAccount())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .email(req.getEmail())
                .nickname(req.getNickname())
                .build();
    }

    @Transactional
    public void softDelete(User user) {
        user.setActivated(false);
        user.setDeletedAt(LocalDateTime.now());

        meetingService.deleteByUser(user);
        voteService.deleteByUser(user);

        userRepository.save(user);
    }

    public Optional<FindIdResponseDto> findUserAccount(String name, String email) {
        Optional<User> optionalUser = userRepository.findByNameAndEmail(name, email);

        // User가 존재하면 아이디를 마스킹 처리하여 DTO로 변환
        return optionalUser.map(user -> {
            String maskedUserAccount = maskUserAccount(user.getUserAccount());
            return new FindIdResponseDto(user.getName(), maskedUserAccount);
        });
    }

    // 아이디 마스킹 로직
    private String maskUserAccount(String userAccount) {
        return userAccount.substring(0, 3) + "*".repeat(userAccount.length() - 3);
    }

    // 임시 비밀번호 전송
    public void sendTemporaryPassword(String userAccount, String name, String email) {
        Optional<User> userOptional = userRepository.findByUserAccountAndNameAndEmail(userAccount, name, email);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("입력하신 정보와 일치하는 사용자가 없습니다.");
        }

        User user = userOptional.get();

        String tempPassword = generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        mailService.sendToServerTempPasswordEmail(user.getEmail(), tempPassword);
    }

    private String generateTempPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }

}
