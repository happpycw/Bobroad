package com.grepp.synapse4.app.model.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoSearchRequestDto {

    private String query;
    private Double latitude;
    private Double longitude;
//    private String url;

}
