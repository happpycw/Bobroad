package com.grepp.synapse4.app.model.meeting.dto;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@Getter @Setter @ToString
public class MeetingDto {

    private Long id;
    private String title;
    private String description;
    private Purpose purpose;
    private Boolean isDutch;
    private Long creatorId;
    private LocalDateTime createdAt = LocalDateTime.now();

}
