package com.grepp.synapse4.app.controller.web.meeting.payload.meeting;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeetingRegistRequest {

  @NotBlank(message = "모임명을 필수로 입력해야 합니다.")
  private String title;

  private String description;

  @NotNull(message = "목적을 필수로 입력해야 합니다.")
  private Purpose purpose;

  private Boolean isDutch = false;

  public MeetingDto toDto(Long userId){
    MeetingDto meetingDto = new MeetingDto();

    meetingDto.setTitle(title);
    meetingDto.setDescription(description);
    meetingDto.setPurpose(purpose);
    meetingDto.setIsDutch(isDutch);
    meetingDto.setCreatorId(userId);

    return meetingDto;
  }

}
