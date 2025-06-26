package com.grepp.synapse4.app.model.llm.entity;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class CurationResult extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curation_result_id")
    private Long id;

    @ColumnDefault("true")
    @Column(name = "active")
    private boolean active=true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curation_id")
    private Curation curation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private String reason;

    protected CurationResult() {

    }

    public CurationResult(String reason, Curation curation, Restaurant restaurant) {
        this.reason = reason;
        this.curation = curation;
        this.restaurant = restaurant;
    }

}
