package com.grepp.synapse4.app.model.auth.domain;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class Principal extends User {

    public Principal(String username, String password,
        Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static Principal createPrincipal(com.grepp.synapse4.app.model.user.entity.User user,
        List<SimpleGrantedAuthority> authorities) {
        return new Principal(user.getUserAccount(), user.getPassword(), authorities);
    }
}
