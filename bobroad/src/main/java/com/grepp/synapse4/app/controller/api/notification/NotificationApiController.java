package com.grepp.synapse4.app.controller.api.notification;

import com.grepp.synapse4.app.controller.web.notification.payload.NotificationStateRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.notification.NotificationService;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationApiController {

    private final NotificationService notificationService;
    private final MeetingService meetingService;

    // 초대 관련 알림 삭제 및 MeetingMember State값 변경
    @DeleteMapping("/remove/{notiId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> remove(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody NotificationStateRequest request
    ){
        Long notiId = request.getNotiId();
        Long meetingId = request.getMeetingId();
        Long userId = userDetails.getUser().getId();
        State state = request.getState();
        
        meetingService.updateInvitedState(meetingId, userId, state);
        notificationService.removeMeetingNotification(notiId);

        return ResponseEntity.ok().build();
    }

}
