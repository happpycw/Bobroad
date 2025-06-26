package com.grepp.synapse4.app.model.llm;

import com.grepp.synapse4.app.model.llm.mongo.RestaurantTagsDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptService {

    public String createTaggingPrompt(String userQuery, String allTagsString) {
        return String.format(
                """
                [지시]
                아래 [전체 태그 목록] 중에서, 사용자의 [질문]과 가장 관련 있는 태그를 최대 5개만 골라 JSON 배열 형태로 반환해줘. 다른 설명은 절대 붙이지 마.

                [전체 태그 목록]
                %s

                [질문]
                %s

                [출력 형식]
                { "tags": ["태그1", "태그2", ...] }
                """, allTagsString, userQuery);
    }


    public String createFinalPrompt(String userText, String prefersText, List<RestaurantTagsDocument> candidates) {
        StringBuilder candidatesTextBuilder = new StringBuilder();
        for (RestaurantTagsDocument doc : candidates) {
            candidatesTextBuilder.append(String.format("- 식당 ID %d: (특징: %s)\n",
                    doc.getRestaurantId(), String.join(", ", doc.getTag())));
        }

        return String.format("""
            [지시]
            너는 사용자의 숨은 취향까지 파악하는 최고의 맛집 추천 전문가야.
            아래 [사용자 취향]과 [사용자 질문]을 모두 고려해서, 주어진 [식당 목록] 중에서 가장 적합한 곳을 3개 추천해줘.
            특히 질문 중에 꼭 '식당을 추천해줘'의 형태가 아니라 '무언갈 먹고 싶다', '어떤 상황에서 먹고 싶다' 등의 표현이 포함된다면, 역시 고려해서 추천해 주면 돼.
            추천하는 이유는 사용자의 취향과 질문과 관계 있는 이유를 들어줘, 입력된 취향은 언급하지 말아줘
            
            혹시 사용자의 질문이 식당 추천과는 관계가 없어보인다면, 입력된 취향에 맞춰서 추천해주면 되고,
            이유에 '무엇을 원하는지 모르시나요? 사용자님의 취향에 맞는 식당을 추천해드립니다'라고 언급해줘.
            
            [사용자 취향]
            %s

            [사용자 질문]
            %s

            [식당 목록]
            %s

            [출력 형식]
            {
                "recommendations": [
                    { "restaurantId": 1, "reason": "혼밥하기 좋고 조용한 분위기" },
                    { "restaurantId": 2, "reason": "프라이빗하고 고급스러운 분위기" }
                    { "restaurantId": 3, "reason": "프라이빗하고 고급스러운 분위기" }
                ]
            }
            """, prefersText, userText, candidatesTextBuilder);
    }



}
