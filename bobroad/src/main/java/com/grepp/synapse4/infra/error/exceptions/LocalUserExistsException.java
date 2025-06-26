package com.grepp.synapse4.infra.error.exceptions;

public class LocalUserExistsException extends RuntimeException {
    public LocalUserExistsException() {
        super("이미 로컬 계정으로 가입된 이메일입니다.");
    }
}
