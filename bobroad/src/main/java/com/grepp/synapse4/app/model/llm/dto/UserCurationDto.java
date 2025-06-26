package com.grepp.synapse4.app.model.llm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserCurationDto {

    private Long id;
    private String title;

    List<CurationRestaurantDto> restaurants;
}
