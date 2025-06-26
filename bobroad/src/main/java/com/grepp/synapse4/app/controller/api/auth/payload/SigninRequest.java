package com.grepp.synapse4.app.controller.api.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {
    
    @NotBlank
    private String username;
    
    @NotBlank
    @Size(min = 4, max = 10)
    private String password;
}
