package com.grepp.synapse4.app.model.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AdminMeetingMemberDto {

    private Long MemberId;
    private Long meetingId;
    private String Nickname;

}
