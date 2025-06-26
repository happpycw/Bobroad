package com.grepp.synapse4.app.model.vote.entity;

import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class VoteMember {

    @Id
    @GeneratedValue
    @Column(name = "vote_member_id")
    private Long id;
    private Boolean isVoted = false;
    private Boolean isJoined;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
