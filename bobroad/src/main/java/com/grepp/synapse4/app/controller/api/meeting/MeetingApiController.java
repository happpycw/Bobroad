package com.grepp.synapse4.app.controller.api.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.meeting.MeetingGrantRequest;
import com.grepp.synapse4.app.controller.web.meeting.payload.meeting.MeetingInviteRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.dto.MeetingMemberDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.notification.NotificationService;
import com.grepp.synapse4.app.model.notification.code.NotificationType;
import com.grepp.synapse4.app.model.notification.dto.NotificationDto;
import com.grepp.synapse4.app.model.notification.entity.Notification;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.infra.response.ApiResponse;
import com.grepp.synapse4.infra.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/meeting")
@RequiredArgsConstructor
@Slf4j
public class MeetingApiController {

    private final MeetingService meetingService;
    private final NotificationService notificationService;
    private final CustomUserDetailsService customUserDetailsService;

    // 모임 초대
    @PostMapping("/invite/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> invite(
        @RequestBody MeetingInviteRequest invite
    ){
        Long meetingId = invite.getMeetingId();
        String account = invite.getAccount();

        Boolean existByUser = customUserDetailsService.findUserByAccount(account);
        if(!existByUser){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ResponseCode.USER_NOT_FOUND));
        }

        Long userId = customUserDetailsService.loadUserIdByAccount(account);
        Boolean existByMeetingMember = meetingService.findMemberByMeetingIdAndUserId(meetingId, userId);
        if(existByMeetingMember){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ResponseCode.USER_ALREADY_INVITED));
        }

        MeetingMemberDto memberDto = new MeetingMemberDto(meetingId, userId);
        Meeting meeting = meetingService.inviteUser(memberDto);

        NotificationDto notiDto = NotificationDto.builder()
                .userId(userId)
                .meeting(meeting)
                .type(NotificationType.MEETING)
                .redirectUrl("meetings/detail/"+meetingId)
                .build();

        Notification notification = notificationService.registNotification(notiDto);
        notificationService.sendNotification(userId, notification, NotificationType.MEETING);

        return ResponseEntity.ok().build();
    }

    // 권한 수정
    @PatchMapping("/grant/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> grant(
        @RequestBody List<MeetingGrantRequest> request
    ){
        meetingService.updateGrantByMember(request);

        return ResponseEntity.ok().build();
    }

}
