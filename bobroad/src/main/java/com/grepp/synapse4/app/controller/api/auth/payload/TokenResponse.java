package com.grepp.synapse4.app.controller.api.auth.payload;

import com.grepp.synapse4.infra.auth.token.code.GrantType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private GrantType grantType;
    private Long expiresIn;
    private Boolean isSurvey;
}
