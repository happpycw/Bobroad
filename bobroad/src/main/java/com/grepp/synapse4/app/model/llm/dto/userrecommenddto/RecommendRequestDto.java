package com.grepp.synapse4.app.model.llm.dto.userrecommenddto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRequestDto {

    private Long userId;
    private String questionText;

}
