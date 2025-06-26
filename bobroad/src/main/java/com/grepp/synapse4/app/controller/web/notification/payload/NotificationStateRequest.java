package com.grepp.synapse4.app.controller.web.notification.payload;

import com.grepp.synapse4.app.model.meeting.code.State;
import lombok.Getter;

@Getter
public class NotificationStateRequest {

    private Long notiId;
    private Long meetingId;
    private State state;

}
