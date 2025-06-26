package com.grepp.synapse4.app.controller.web.meeting.payload.meeting;

import lombok.Getter;

@Getter
public class MeetingInviteRequest {

  private Long meetingId;
  private String account;

}