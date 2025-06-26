package com.grepp.synapse4.app.model.llm.entity;

import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LLMQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column
    private String text;

    @Column
    private Long userId;

    // question에 대한 여러 result의 list를 조회해줘야 하므로 양방향 매핑
    @OneToMany(mappedBy = "llmQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LLMResult> results = new ArrayList<>();
}
