package com.grepp.synapse4.app.model.restaurant.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class KakaoSearchResponseDto {

    private List<KakaoPlace> documents;
    private Meta meta;

    @Getter
    @Setter
    private static class Meta {
        private Integer total_count;
        private Integer pageable_count;
        private Boolean is_end;
        private SameName sameName;
    }

    @Getter
    @Setter
    public static class KakaoPlace {
        private String id;
        private String place_name;
        private String category_name;
        private String phone;
        private String x;       // 경도
        private String y;       // 위도
        private String place_url;
        private String distance;
    }

    @Getter
    @Setter
    private static class SameName {
        private String[] region;
        private String keyword;
        private String selected_region;
    }
}
