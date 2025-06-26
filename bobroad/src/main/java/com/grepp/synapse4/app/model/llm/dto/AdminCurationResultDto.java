package com.grepp.synapse4.app.model.llm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder

public class AdminCurationResultDto {

    private Long id;
    private String title;
    private String name;
    private String address;
    private boolean active;
    private LocalDateTime createdAt;

}