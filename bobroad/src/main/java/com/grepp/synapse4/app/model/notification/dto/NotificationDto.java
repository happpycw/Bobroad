package com.grepp.synapse4.app.model.notification.dto;

import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.notification.code.NotificationType;
import com.grepp.synapse4.app.model.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationDto {

    private Long userId;
    private Meeting meeting;
    private Vote vote;
    private NotificationType type;
    private String redirectUrl;

    public static NotificationDto from(Notification n){
        return NotificationDto.builder()
            .userId(n.getUserId())
            .meeting(n.getMeeting())
            .vote(n.getVote())
            .type(n.getType())
            .redirectUrl(n.getRedirectURL())
            .build();
    }

}
