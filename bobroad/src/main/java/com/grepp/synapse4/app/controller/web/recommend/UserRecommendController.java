package com.grepp.synapse4.app.controller.web.recommend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grepp.synapse4.app.model.llm.LlmQuestionService;
import com.grepp.synapse4.app.model.llm.LlmResultService;
import com.grepp.synapse4.app.model.llm.RecommendationService;
import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.RecommendedRestaurantDto;
import com.grepp.synapse4.app.model.llm.entity.LLMQuestion;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserRecommendController {

    private final LlmQuestionService llmQuestionService;
    private final LlmResultService llmResultService;
    private final RecommendationService recommendationService;


    @GetMapping("/recommend")
    public String recommendStarter() {
        return "recommend/recommend-llm";
    }

    @PostMapping("/recommend/init")
    public String refRecommendStarter(@RequestParam String questionText,
                                      @AuthenticationPrincipal CustomUserDetails user) throws JsonProcessingException {

        // 1. userId 갖고 오기
        Long userId = user.getUser().getId();

        // 2. 질문 저장
        LLMQuestion question = llmQuestionService.saveQuestionText(userId, questionText);

        // 3. Gemini 호출 및 결과 파싱
        recommendationService.recommendation(question.getId(), question.getText(), userId);

        return "redirect:/recommend/result?questionId=" + question.getId();
    }

    @GetMapping("/recommend/result")
    public String showRef(@RequestParam Long questionId, Model model) {
        List<RecommendedRestaurantDto> results = llmResultService.getRecommendedResults(questionId);
        model.addAttribute("results", results);
        return "recommend/recommend-result";
    }
}
