package com.grepp.synapse4.infra.error.exceptions;

import lombok.Getter;

@Getter
public class NeedNicknameRegistrationException extends RuntimeException {
    private final String email;

    public NeedNicknameRegistrationException(String email) {
        super("닉네임 설정이 필요한 사용자입니다.");
        this.email = email;
    }

}
