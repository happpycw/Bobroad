package com.grepp.synapse4.app.controller.web.vote.payload;

import com.grepp.synapse4.app.model.vote.dto.VoteDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VoteRegistRequest {

  private Long meetingId;
  @NotBlank(message = "투표 제목 입력해주세요.")
  private String title;
  private String description;
  @NotNull(message = "모임 날짜를 설정해주세요.")
  private LocalDateTime meetingDate;
  @NotNull(message = "투표 마감날짜를 설정해주세요.")
  private LocalDateTime endedAt;
  private Boolean isDutch = false;
  @NotNull(message = "식당 1개를 필수로 선택해주세요.")
  private Long restaurantId;

  public VoteDto toDto(){
    VoteDto voteDto = new VoteDto();

    voteDto.setMeetingId(meetingId);
    voteDto.setTitle(title);
    voteDto.setDescription(description);
    voteDto.setMeetingDate(meetingDate);
    voteDto.setEndedAt(endedAt);
    voteDto.setIsDutch(isDutch);
    voteDto.setRestaurantId(restaurantId);

    return voteDto;
  }

}
