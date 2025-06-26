package com.grepp.synapse4.app.model.llm.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiRequestDto {
// 우리가 제미나이에게 보내는 요청

    private List<Content> contents;

    public GeminiRequestDto(String userText) {
        this.contents = List.of(new Content("user", List.of(new Part(userText))));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Content {
        private String role;
        private List<Part> parts;

    }

    // 여기가 진짜 사용자 입력 값
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class Part {
        private String text;
    }
}
