package com.grepp.synapse4.infra.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {
    OK("0000", HttpStatus.OK, "정상적으로 완료되었습니다."),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    UNAUTHORIZED("4001", HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 입니다."),
    USER_NOT_FOUND("40401", HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    USER_ALREADY_INVITED("40901", HttpStatus.CONFLICT, "이미 초대된 유저입니다."),
    BAD_CREDENTIAL("4011", HttpStatus.UNAUTHORIZED, "아이디나 비밀번호가 틀렸습니다."),
    INVALID_TOKEN("4002", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    SECURITY_INCIDENT("6000", HttpStatus.UNAUTHORIZED, "계정에 비정상적인 접근이 감지되었습니다."),
    VOTE_ENDED("40501", HttpStatus.CONFLICT, "마감된 투표입니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;
    
    ResponseCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
    
    public String code() {
        return code;
    }
    
    public HttpStatus status() {
        return status;
    }
    
    public String message() {
        return message;
    }
}
