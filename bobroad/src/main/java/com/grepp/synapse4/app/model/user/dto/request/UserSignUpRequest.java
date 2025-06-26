package com.grepp.synapse4.app.model.user.dto.request;

import com.grepp.synapse4.app.model.auth.code.Role;
import com.grepp.synapse4.app.model.auth.code.Provider;
import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequest {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z0-9]{6,16}$", message = "아이디는 공백 없이 영어 소문자와 숫자를 모두 포함한 6~16자여야 합니다.")
    private String userAccount;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[^\\s]{4,16}$", message = "비밀번호는 공백 없이 영문자와 숫자를 포함한 4~16자여야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자여야 합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    public UserSignUpRequest() {

    }

    public User toEntity(String encodedPassword, Role role) {
        return User.builder()
            .userAccount(this.userAccount)
            .password(encodedPassword)
            .name(this.name)
            .nickname(this.nickname)
            .email(this.email)
            .isSurvey(false)
            .activated(true)
            .role(role)
            .provider(Provider.LOCAL)
            .providerId(null)
            .build();
    }
}
