package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void validateIdentifiers(String userAccount, String email, String nickname, Long excludeUserId, BindingResult bindingResult) {
        if (userAccount != null && userRepository.existsByUserAccount(userAccount)) {
            bindingResult.rejectValue("userAccount", "duplicate", "이미 등록된 아이디입니다.");
        }

        userRepository.findByEmail(email)
            .filter(u -> !u.getId().equals(excludeUserId))
            .ifPresent(u -> bindingResult.rejectValue("email", "duplicate", "이미 등록된 이메일입니다."));

        userRepository.findByNickname(nickname)
            .filter(u -> !u.getId().equals(excludeUserId))
            .ifPresent(u -> bindingResult.rejectValue("nickname", "duplicate", "이미 사용 중인 닉네임입니다."));
    }

}
