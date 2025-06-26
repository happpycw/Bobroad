package com.grepp.synapse4.app.model.llm.dto.userrecommenddto;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendedRestaurantDto {
    Restaurant restaurant;
    String reason;

}
