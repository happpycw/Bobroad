package com.grepp.synapse4.app.model.restaurant.dto.search;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRestaurantRequestDto {

    @NotBlank(message = "입력값은 필수입니다.")
    private String restaurantKeyword;
}
