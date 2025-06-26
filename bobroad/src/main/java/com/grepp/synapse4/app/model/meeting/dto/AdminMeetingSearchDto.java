package com.grepp.synapse4.app.model.meeting.dto;

import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminMeetingSearchDto {

    private Long id;
    private String title;
    private Purpose purpose;
    private String description;
    private User user;
    private boolean isDutch;

}
