package com.grepp.synapse4.app.model.llm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GeminiUserRequestDto {

    @NotBlank(message = "입력값은 필수입니다.")
    private String userText;
}
