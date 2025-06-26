package com.grepp.synapse4.app.model.llm.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCurationSurveyDto {

    private Long id;
    private String title;
    // 생성자 파라미터와 필드 타입을 List<RestaurantDto>로 통일
    private List<RestaurantDto> restaurants;

    @Getter
    @ToString
    @AllArgsConstructor
    public static class RestaurantDto {
        private Long id;
        private String name;
        private String category;
        private String roadAddress;
        private String branch;
        private String reason;
        private String businessTime;
    }
}
