package com.grepp.synapse4.app.model.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.GeminiFullResponseDto;
import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.GeminiResponseDto;
import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.GeminiTagResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LLMResponseParserService {


    // 입력값에 알맞는 태그 llm 결과 파싱
    public List<String> parseToTagList(String tags) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // 1. 필요없는 json 외벽 없애기
        GeminiFullResponseDto fullDto = mapper.readValue(tags, GeminiFullResponseDto.class);

        // 2. 결과 값 추출
        String innerJsonString = fullDto.getCandidates()
                .getFirst()
                .getContent()
                .getParts()
                .getFirst()
                .getText();

        // 백틱 제거
        String cleanedJson = innerJsonString.replaceAll("```json", "")
                .replaceAll("```", "").trim();

        // 3. taggingResponseDto 파싱
        GeminiTagResponseDto taggingResponse = mapper.readValue(cleanedJson, GeminiTagResponseDto.class);

        return taggingResponse.tags();
    }


    // 진짜 llm 추천 파싱
    public GeminiResponseDto parseGeminiRecommendResponse(String geminiResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // full response dto 파싱
            GeminiFullResponseDto fullDto = mapper.readValue(geminiResponse, GeminiFullResponseDto.class);

            // String json get
            String rawJson = fullDto.getCandidates()
                    .getFirst()
                    .getContent()
                    .getParts()
                    .getFirst()
                    .getText();

            // 영원히 돌아오는 백틱 제거
            String cleanedJson = rawJson.replaceAll("```json", "")
                    .replaceAll("```", "").trim();

            // json일 때만 시도(아니면... 일단 임시 리스트 반환)
            if (cleanedJson.startsWith("{") || cleanedJson.startsWith("[")) {
                return mapper.readValue(cleanedJson, GeminiResponseDto.class);
            } else {
                log.warn("llm 응답 오류로 임시 식당 데이터 대체: {}", cleanedJson);

                GeminiResponseDto.Recommendation res1 = new GeminiResponseDto.Recommendation(9L, "인기 있는 식당 1");
                GeminiResponseDto.Recommendation res2 = new GeminiResponseDto.Recommendation(10L, "인기 있는 식당 2");
                GeminiResponseDto.Recommendation res3 = new GeminiResponseDto.Recommendation(11L, "인기 있는 식당 3");

                List<GeminiResponseDto.Recommendation> fallbackRecommendations = List.of(res1, res2, res3);

                return new GeminiResponseDto(fallbackRecommendations);
            }

        } catch (Exception e) {
            throw new RuntimeException("❗ Gemini 응답 파싱 실패: " + e.getMessage(), e);
        }
    }
}
