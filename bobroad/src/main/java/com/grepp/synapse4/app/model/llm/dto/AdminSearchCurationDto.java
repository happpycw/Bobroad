package com.grepp.synapse4.app.model.llm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AdminSearchCurationDto {
    private Long curationResultId;
    private String title;
    private Long restaurantId;
    private String restaurantName;
    private String category;
    private String address;
    private String branch;

    @Builder.Default
    private boolean active = true;
}
