package com.grepp.synapse4.app.model.meeting.dto;

import com.grepp.synapse4.app.model.meeting.code.State;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
public class MeetingMemberDto {
  private Long meetingId;
  private Long userId;
  private State state = State.WAIT;
  private LocalDateTime createdAt = LocalDateTime.now();
  private LocalDateTime deletedAt = null;

  public MeetingMemberDto(Long meetingId, Long userId) {
    this.meetingId = meetingId;
    this.userId = userId;
  }
}

