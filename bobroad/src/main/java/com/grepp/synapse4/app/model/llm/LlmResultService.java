package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.dto.userrecommenddto.RecommendedRestaurantDto;
import com.grepp.synapse4.app.model.llm.entity.LLMResult;
import com.grepp.synapse4.app.model.llm.repository.LlmResultRepository;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LlmResultService {

    private final LlmResultRepository llmResultRepository;

    // dto 제작 과정에서 생겨난 의존성...
    private final RestaurantRepository restaurantRepository;

    public List<RecommendedRestaurantDto> getRecommendedResults(Long questionId) {
        List<LLMResult> results = llmResultRepository.findByLlmQuestionId(questionId);

        return results.stream()
                .map(result -> {
                    Restaurant restaurant = restaurantRepository.findById(result.getRestaurantId())
                            .orElseThrow(() -> new RuntimeException("식당 없음"));
                    return new RecommendedRestaurantDto(restaurant, result.getReason());
                })
                .toList();
    }
}
