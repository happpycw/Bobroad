package com.grepp.synapse4.app.model.restaurant.dto;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestaurantDto {
    private Long id;
    private String name;
    private double latitude;
    private double longitude;


    public static RestaurantDto from(Restaurant r) {
        return new RestaurantDto(r.getId(), r.getName(), r.getLatitude(), r.getLongitude());
    }

}
