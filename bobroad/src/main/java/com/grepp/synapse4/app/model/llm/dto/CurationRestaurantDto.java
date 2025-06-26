package com.grepp.synapse4.app.model.llm.dto;

import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurationRestaurantDto {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;

    private String category;
    private String roadAddress;
    private String businessTime;
    private String dayOff;
    private String tel;
    private String branch;

    private String reason;

    public static CurationRestaurantDto fromEntity(Restaurant restaurant, CurationResult curationResult) {
        return new CurationRestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getCategory(),
                restaurant.getAddress(),
                restaurant.getDetail().getRowBusinessTime(),
                restaurant.getDetail().getDayOff(),
                restaurant.getDetail().getTel(),
                restaurant.getBranch(),
                curationResult.getReason()
        );
    }

}
