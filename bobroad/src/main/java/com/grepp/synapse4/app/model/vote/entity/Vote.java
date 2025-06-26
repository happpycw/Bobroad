package com.grepp.synapse4.app.model.vote.entity;

import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Vote extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "vote_id")
    private Long id;
    private String title;
    private String description;
    private LocalDateTime meetingDate;
    private LocalDateTime endedAt;
    private Boolean isDutch;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    // TODO: 한 투표에서 여러 식당을 투표하는 기능 추가
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteMember> voteMembers = new ArrayList<>();
}
