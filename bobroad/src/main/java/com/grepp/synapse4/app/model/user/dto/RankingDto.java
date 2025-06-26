package com.grepp.synapse4.app.model.user.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter @Setter @ToString

public class RankingDto {
    private Long   id;
    private Long   bookmarkCount;
    private String name;
    private String address;
    private String branch;
    private String category;
    private String businessTime;

    public RankingDto(Long id, Long bookmarkCount, String name, String address, String branch, String category, String businessTime) {
        this.id            = id;
        this.bookmarkCount = bookmarkCount;
        this.name          = name;
        this.address       = address;
        this.branch        = branch;
        this.category      = category;
        this.businessTime  = businessTime;
    }
}
