package com.grepp.synapse4.app.model.restaurant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Getter
@ToString
public class RestaurantMenuImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "menu_image")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private RestaurantMenu menu;

}
