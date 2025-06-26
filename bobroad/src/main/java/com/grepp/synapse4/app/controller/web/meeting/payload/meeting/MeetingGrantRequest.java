package com.grepp.synapse4.app.controller.web.meeting.payload.meeting;

import com.grepp.synapse4.app.model.meeting.code.Role;
import lombok.Getter;

@Getter
public class MeetingGrantRequest {

    private Long memberId;
    private Role role;

}
