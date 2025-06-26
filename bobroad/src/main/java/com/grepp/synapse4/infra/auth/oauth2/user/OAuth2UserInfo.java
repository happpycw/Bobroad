package com.grepp.synapse4.infra.auth.oauth2.user;

import com.grepp.synapse4.app.model.auth.code.Provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    Provider getProvider();
}
