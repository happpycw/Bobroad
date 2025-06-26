package com.grepp.synapse4.app.model.restaurant.dto;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.entity.RestaurantMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuDto {

    private String menuName;
    private Integer price;

    public RestaurantMenu toEntity(Restaurant restaurant) {
        return new RestaurantMenu(menuName, price, restaurant);
    }

}
