package com.grepp.synapse4.infra.auth.oauth2.user;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo create(String registrationId, OAuth2User user) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(user);
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth2 제공자입니다: " + registrationId);
        };
    }
}
