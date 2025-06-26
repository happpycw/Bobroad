package com.grepp.synapse4.app.model.restaurant.dto.create;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CsvRestaurantDetailDto {

    private String publicId;
    private String name;
    private String branch;
    private String regionName;
    private String parkingRaw;
    private String wifiRaw;
    private String playroom;
    private String foreignMenu;
    private String toiletInfo;
    private String dayOff;
    private String rowBusinessTime;
    private String deliveryRaw;
    private String onlineReservationInfo;
    private String homePageURL;
    private String landmarkName;
    private String landmarkLatitude;
    private String landmarkLongitude;
    private String distanceFromLandmark;
    private String smartOrder;
    private String signatureMenus;
    private String status;
    private String hashtags;
    private String areaInfo;

    // 원본 값 Boolean화
    public Boolean getParking() {
        return "Y".equalsIgnoreCase(parkingRaw);
    }

    public Boolean getWifi() {
        return "Y".equalsIgnoreCase(wifiRaw);
    }

    public Boolean getDelivery() {
        return "Y".equalsIgnoreCase(deliveryRaw);
    }
}