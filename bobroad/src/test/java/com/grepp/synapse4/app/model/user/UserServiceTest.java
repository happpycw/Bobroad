package com.grepp.synapse4.app.model.user;

import com.grepp.synapse4.app.model.user.dto.request.UserSignUpRequest;
import com.grepp.synapse4.app.model.user.dto.response.FindIdResponseDto;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findUser() {
        // given
        String name = "홍길동";
        String email = "hong@test.com";
        String userAccount = "honggildong";

        User user = User.builder()
                .name(name)
                .email(email)
                .userAccount(userAccount)
                .build();

        given(userRepository.findByNameAndEmail(name, email))
                .willReturn(Optional.of(user));

        // when
        Optional<FindIdResponseDto> result = userService.findUserAccount(name, email);

        // then
        assertThat(result).isPresent();
        FindIdResponseDto dto = result.get();
        System.out.println("마스킹된 아이디: " + dto.getMaskedUserAccount());
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getMaskedUserAccount()).startsWith("hon").contains("*");
    }

    @Test
    void signup() {
        // given
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUserAccount("chickenisgood");
        request.setPassword("qwe123~");
        request.setName("정상수");
        request.setNickname("꼬꼬닭");
        request.setEmail("chickenisgood@gmail.com");

        String encodedPassword = "encodedPw123";
        when(passwordEncoder.encode("qwe123~")).thenReturn(encodedPassword);

        // when
        userService.signup(request);

        // then
        System.out.println("입력값 확인");
        System.out.println(">>> 아이디: " + request.getUserAccount());
        System.out.println(">>> 이름: " + request.getName());
        System.out.println(">>> 닉네임: " + request.getNickname());
        System.out.println(">>> 이메일: " + request.getEmail());

        assertThat(request.getUserAccount()).isEqualTo("chickenisgood");
        assertThat(request.getName()).isEqualTo("정상수");
        assertThat(request.getNickname()).isEqualTo("꼬꼬닭");
        assertThat(request.getEmail()).isEqualTo("chickenisgood@gmail.com");
    }
}