package com.grepp.synapse4.app.model.vote.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@Getter @Setter @ToString
public class VoteDto {

  private Long meetingId;
  private String title;
  private String description;
  private LocalDateTime meetingDate;
  private LocalDateTime endedAt;
  private Boolean isDutch;
  private Long restaurantId;
//  private List<Long> selectedRestaurantList;

}
