package com.grepp.synapse4.app.model.llm.dto.userrecommenddto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GeminiResponseDto {

    private List<Recommendation> recommendations;

    // 식당id + 이유 받기 위한 디티오
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Recommendation {
        private Long restaurantId;
        private String reason;
    }
}
