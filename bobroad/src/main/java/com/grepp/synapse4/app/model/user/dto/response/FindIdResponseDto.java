package com.grepp.synapse4.app.model.user.dto.response;

import lombok.Getter;

@Getter
public class FindIdResponseDto {
    private String name;
    private String maskedUserAccount;

    public FindIdResponseDto(String name, String maskedUserAccount) {
        this.name = name;
        this.maskedUserAccount = maskedUserAccount;
    }
}
