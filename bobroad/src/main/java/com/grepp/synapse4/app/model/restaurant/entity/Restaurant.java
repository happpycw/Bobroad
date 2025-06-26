package com.grepp.synapse4.app.model.restaurant.entity;

import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicInsert
@Getter
@ToString
@NoArgsConstructor
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false, name = "restaurant_name")
    private String name;

    @Column(name = "branch")
    private String branch;

    @Column(name = "restaurant_road_address")
    private String address;

    @Column(name = "restaurant_jibun_address")
    private String jibunAddress;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column
    private String category;

    @Setter
    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean activated = false;

    @Setter
    private String kakaoId;

    private String publicId;

    @Setter
    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private RestaurantDetail detail;

    @Setter
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<RestaurantMenu> menus = new ArrayList<>();

    @Builder
    public Restaurant(Long id, String name, String branch, String roadAddress, String jibunAddress, Double latitude, Double longitude, String category, Boolean activated, String publicId, String kakaoId, RestaurantDetail detail) {
        this.id = id;
        this.name = name;
        this.branch = branch;
        this.address = roadAddress;
        this.jibunAddress = jibunAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.activated = activated;
        this.publicId = publicId;
        this.kakaoId = kakaoId;
        this.detail = detail;
    }

}
