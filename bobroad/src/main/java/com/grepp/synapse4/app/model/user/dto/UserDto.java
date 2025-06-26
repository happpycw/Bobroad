package com.grepp.synapse4.app.model.user.dto;

import com.grepp.synapse4.app.model.auth.code.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString

public class UserDto {

    private Long userId;
    private String userAccount;
    private String password;
    private String nickname;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isSurvey;
}
