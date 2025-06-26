package com.grepp.synapse4.app.model.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grepp.synapse4.app.model.llm.repository.RestaurantTagsDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaggingService {
    // 입력된 text에서, mongoDB에 저장된 tags 중 입력된 text와 가장 유사한 tags를 선택하게끔 하는 Service

    private final GeminiService geminiService;
    private final RestaurantTagsDocumentRepository restaurantRepository;
    private final LLMResponseParserService llmResponseParserService;
    private final PromptService promptService;

    public List<String> taggingFromText(String userText) throws JsonProcessingException {

        // 1. mongo에서 전체 태그 목록 조회
        List<String> allTags = restaurantRepository.findDistinctTag();

        // 1-2. 전체 태그 목록, "단어, 단어, " 형태로 String
        String allTagsString = allTags.stream()
                .map(tag -> "\"" + tag + "\"")
                .collect(Collectors.joining(", "));

        // 2. 프롬프트 생성
        String prompt = promptService.createTaggingPrompt(userText, allTagsString);

        // 3. GeminiService를 통해 gem 호출
        String response = geminiService.getGeminiResponse(prompt);

        // 4. 응답 파싱
        return llmResponseParserService.parseToTagList(response);
    }
}