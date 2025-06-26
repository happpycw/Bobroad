package com.grepp.synapse4.app.model.meeting.dto;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.user.entity.User;
import lombok.*;


@Getter
@Builder
@AllArgsConstructor

public class AdminMeetingDto {

    private Long id;
    private String title;
    private User user;
    private String description;
    private boolean isDutch;
    private Purpose purpose;

}
