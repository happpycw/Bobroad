package com.grepp.synapse4.app.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminUserSearchDto {

    private Long id;
    private String userAccount;
    private String name;
    private String password;
    private String nickname;
    private String email;

}
