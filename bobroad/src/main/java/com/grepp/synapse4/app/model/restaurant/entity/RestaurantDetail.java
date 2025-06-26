package com.grepp.synapse4.app.model.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Entity
@DynamicInsert
@Getter
@ToString
@NoArgsConstructor
public class RestaurantDetail {

    @Id
    @Setter
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column
    private String dayOff;

    @Column
    private String rowBusinessTime;

    @Column(name = "restaurant_tel")
    private String tel;

    @Column
    private Boolean parking;

    @Column
    private Boolean wifi;

    @Column
    private Boolean delivery;

    @Column
    @Setter
    private String homePageURL;

    @Builder
    public RestaurantDetail(Restaurant restaurant, String dayOff, String rowBusinessTime, String tel, Boolean parking, Boolean wifi, Boolean delivery, String homePageURL) {
        this.restaurant = restaurant;
        this.dayOff = dayOff;
        this.rowBusinessTime = rowBusinessTime;
        this.tel = tel;
        this.parking = parking;
        this.wifi = wifi;
        this.delivery = delivery;
        this.homePageURL = homePageURL;
    }
}
