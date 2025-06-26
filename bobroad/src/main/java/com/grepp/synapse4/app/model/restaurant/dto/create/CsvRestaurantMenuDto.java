package com.grepp.synapse4.app.model.restaurant.dto.create;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CsvRestaurantMenuDto {

    private String publicMenuId;
    private String menuName;
    private Integer menuPrice;
    private String isLocalSpecialty;
    private String localSpecialtyName;
    private String localSpecialtySourceUrl;
    private String regionName;
    private String publicId;
    private String restaurantName;
    private String branchName;
}