package com.grepp.synapse4.app.model.notification.entity;

import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.notification.code.NotificationType;
import com.grepp.synapse4.infra.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;

@Entity
@DynamicInsert
@Data @ToString
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "is_sended")
    private Boolean isSended = true;

    @Column(name = "redirect_URL")
    private String redirectURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_id")
    private Vote vote;
}
