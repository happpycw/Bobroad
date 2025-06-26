package com.grepp.synapse4.app.model.llm.entity;

import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Curation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curation_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String companyLocation;

    @Column(nullable = false)
    private String purpose;

    @Column(nullable = false)
    private String companion;

    @Column(nullable = false)
    private String favoriteCategory;

    @Column(nullable = false)
    private String preferredMood;

    @ColumnDefault("true")
    private Boolean activated = true;

    @OneToMany(mappedBy = "curation", cascade = CascadeType.ALL)
    private List<CurationResult> results = new ArrayList<>();

}
