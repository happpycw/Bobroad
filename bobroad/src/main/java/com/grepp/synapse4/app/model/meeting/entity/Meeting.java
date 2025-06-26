package com.grepp.synapse4.app.model.meeting.entity;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Getter @Setter
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private Purpose purpose;
    private Boolean isDutch;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User user;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingMember> meetingMembers = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    @Override
    public String toString() {
        return "Meeting{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", purpose=" + purpose +
            ", isDutch=" + isDutch +
            ", user=" + user +
            ", createdAt=" + createdAt +
            '}';
    }
}