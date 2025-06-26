package com.grepp.synapse4.infra.error.exceptions;

import com.grepp.synapse4.infra.response.ResponseCode;

public class AuthApiException extends CommonException{
    
    public AuthApiException(ResponseCode code) {
        super(code);
    }
    
    public AuthApiException(ResponseCode code, Exception e) {
        super(code, e);
    }
}
