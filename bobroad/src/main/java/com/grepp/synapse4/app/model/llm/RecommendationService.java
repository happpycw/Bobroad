package com.grepp.synapse4.app.model.llm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.GeminiResponseDto;
import com.grepp.synapse4.app.model.llm.entity.Curation;
import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import com.grepp.synapse4.app.model.llm.entity.LLMQuestion;
import com.grepp.synapse4.app.model.llm.entity.LLMResult;
import com.grepp.synapse4.app.model.llm.mongo.RestaurantTagsDocument;
import com.grepp.synapse4.app.model.llm.repository.CurationRepository;
import com.grepp.synapse4.app.model.llm.repository.CurationResultRepository;
import com.grepp.synapse4.app.model.llm.repository.LlmQuestionRepository;
import com.grepp.synapse4.app.model.llm.repository.LlmResultRepository;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import com.grepp.synapse4.app.model.user.entity.Survey;
import com.grepp.synapse4.app.model.user.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final LlmQuestionRepository llmQuestionRepository;
    private final LlmResultRepository llmResultRepository;
    private final SurveyRepository surveyRepository;
    private final RestaurantRepository restaurantRepository;
    private final CurationResultRepository curationResultRepository;

    private final TaggingService taggingService;
    private final RestaurantCandidateFromMongoService restaurantCandidateService;
    private final PromptService promptService;
    private final GeminiService geminiService;
    private final LLMResponseParserService llmResponseParserService;
    private final CurationRepository curationRepository;


    //todo curation 재사용 예정
    public void recommendation(Long questionId, String userText, Long userId) throws JsonProcessingException {

        //1. 태그 추출
        List<String> tags = taggingService.taggingFromText(userText);

        //2. mongo 검색
        List<RestaurantTagsDocument> candidates = restaurantCandidateService.findRestaurantsByTags(tags);

        //3. 입력한 사용자의 survey 결과를 string으로 바꿈
        Optional<Survey> surveyOptional = surveyRepository.findByUserId(userId);
        String prefersText;
        if (surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            prefersText = this.surveyToPromptText(survey);
        } else {
            prefersText = "별도로 없음";
        }

        //4. 최종 프롬프트 생성
        String finalPrompt = promptService.createFinalPrompt(userText, prefersText, candidates);

        //5. LLM 호출
        String llmResponse = geminiService.getGeminiResponse(finalPrompt);

        //6. 응답 파싱
        GeminiResponseDto responseDto = llmResponseParserService.parseGeminiRecommendResponse(llmResponse);

        //7. 응답 저장!
        saveResults(questionId, responseDto.getRecommendations());

    }


    // curation recommendation process 오버로딩
    public void recommendation(Long curationId, String curationTitle) throws JsonProcessingException {

        //1. 태그 추출
        List<String> tags = taggingService.taggingFromText(curationTitle);

        //2. mongo 검색
        List<RestaurantTagsDocument> candidates = restaurantCandidateService.findRestaurantsByTags(tags);

        //3. 입력한 사용자의 survey 결과를 string으로 바꿈
        //-> 입력된 curation의 설정 결과를 string으로 바꿈
        Optional<Curation> curationOptional = curationRepository.findById(curationId);
        String prefersText;
        if (curationOptional.isPresent()) {
            Curation curation = curationOptional.get();
            prefersText = this.curationFormToPromptText(curation);
        } else {
            prefersText = "별도로 없음";
        }

        //4. 최종 프롬프트 생성
        String finalPrompt = promptService.createFinalPrompt(curationTitle, prefersText, candidates);

        //5. LLM 호출
        String llmResponse = geminiService.getGeminiResponse(finalPrompt);

        //6. 응답 파싱
        GeminiResponseDto responseDto = llmResponseParserService.parseGeminiRecommendResponse(llmResponse);

        //7. 응답 저장!
        //-> 마찬가지로 curationResult에 저장
        saveCurationResults(curationId, responseDto);

    }

    // todo 내부 메서드.. 고민 중
    // 4. 도착한 응답 result DB 에 저장(curation)
    protected void saveCurationResults(Long curationId, GeminiResponseDto responseDto) {
        Curation curation = curationRepository.findById(curationId)
                .orElseThrow(() -> new RuntimeException("질문이 존재하지 않습니다"));

        List<CurationResult> results = new ArrayList<>();

        for (GeminiResponseDto.Recommendation rec : responseDto.getRecommendations()) {
            Restaurant restaurant = restaurantRepository.findById(rec.getRestaurantId())
                    .orElseThrow(() -> new RuntimeException("식당 없음"));

            results.add(new CurationResult(rec.getReason(), curation, restaurant));
        }

        curationResultRepository.saveAll(results);
    }


    // todo 내부 메서드.. 고민 중
    // 4. 도착한 응답 result DB 에 저장(userRecommend)
    protected void saveResults(Long questionId, List<GeminiResponseDto.Recommendation> recommendations) {
        LLMQuestion question = llmQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("질문이 존재하지 않습니다"));

        List<LLMResult> results = recommendations.stream()
                .map(rec -> LLMResult.builder()
                        .reason(rec.getReason())
                        .restaurantId(rec.getRestaurantId())
                        .llmQuestion(question)
                        .build())
                .toList();

        llmResultRepository.saveAll(results);
    }


    private String surveyToPromptText(Survey survey) {
        StringBuilder sb = new StringBuilder();
        if(!ObjectUtils.isEmpty(survey.getPurpose())){
            sb.append(String.format("주목적은 '%s'입니다. ", survey.getPurpose()));
        }
        if(!ObjectUtils.isEmpty(survey.getCompanion())){
            sb.append(String.format("'%s'와(과) 함께 방문합니다. ", survey.getCompanion()));
        }
        if(!ObjectUtils.isEmpty(survey.getFavoriteCategory())){
            sb.append(String.format("선호하는 음식 카테고리는 '%s'입니다. ", survey.getFavoriteCategory()));
        }
        if(!ObjectUtils.isEmpty(survey.getPreferredMood())){
            sb.append(String.format("선호하는 분위기는 '%s'입니다. ", survey.getPreferredMood()));
        }

        if (sb.isEmpty()) {
            return "별도로 없음";
        }

        return sb.toString().trim();
    }


    private String curationFormToPromptText(Curation curation) {
        StringBuilder sb = new StringBuilder();
        if(!ObjectUtils.isEmpty(curation.getPurpose())){
            sb.append(String.format("주목적은 '%s'입니다. ", curation.getPurpose()));
        }
        if(!ObjectUtils.isEmpty(curation.getCompanion())){
            sb.append(String.format("'%s'와(과) 함께 방문합니다. ", curation.getCompanion()));
        }
        if(!ObjectUtils.isEmpty(curation.getFavoriteCategory())){
            sb.append(String.format("선호하는 음식 카테고리는 '%s'입니다. ", curation.getFavoriteCategory()));
        }
        if(!ObjectUtils.isEmpty(curation.getPreferredMood())){
            sb.append(String.format("선호하는 분위기는 '%s'입니다. ", curation.getPreferredMood()));
        }

        if (sb.isEmpty()) {
            return "별도로 없음";
        }

        return sb.toString().trim();
    }

}