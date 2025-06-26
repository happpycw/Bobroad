package com.grepp.synapse4.app.model.auth.token.dto;

import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long atExpiresIn;
    private Long rtExpiresIn;
    private User user;
}

