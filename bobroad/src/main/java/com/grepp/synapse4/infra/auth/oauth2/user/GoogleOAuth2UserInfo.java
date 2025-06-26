package com.grepp.synapse4.infra.auth.oauth2.user;

import com.grepp.synapse4.app.model.auth.code.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2UserInfo implements OAuth2UserInfo {

    private final OAuth2User oAuth2User;

    public GoogleOAuth2UserInfo(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public String getProviderId() {
        return oAuth2User.getAttribute("sub"); // 구글의 고유 ID
    }

    @Override
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }
}

