package com.grepp.synapse4.app.model.restaurant.dto.create;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CsvRestaurantDto {

    private String publicId;         // 식당(ID)
    private String name;             // 식당명
    private String branch;           // 지점명
    private String address;      // 도로명주소
    private String jibunAddress;     // 지번주소
    private Double latitude;         // 식당위도
    private Double longitude;        // 식당경도
    private String tel;              // 식당대표전화번호
    private String category;         // 영업신고증업태명
    private String businessName;     // 영업인허가명
    private String description;      // 식당소개내용
}
