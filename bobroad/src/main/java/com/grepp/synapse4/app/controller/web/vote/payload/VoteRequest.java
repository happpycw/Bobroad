package com.grepp.synapse4.app.controller.web.vote.payload;

import lombok.Data;

@Data
public class VoteRequest {
    private Long voteId;
    private Boolean isJoined;
}
