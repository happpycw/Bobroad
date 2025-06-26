package com.grepp.synapse4.app.controller.web.notification;

import com.grepp.synapse4.app.model.notification.NotificationService;
import com.grepp.synapse4.app.model.notification.dto.NotificationDto;
import com.grepp.synapse4.app.model.notification.entity.Notification;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.infra.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Controller
@Slf4j
@RequestMapping("notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // 유저 alarm 접속 시 userId로 SseEmitter와 연결
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter send(
            @AuthenticationPrincipal CustomUserDetails userDetails
            // @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") final String lastEventId
    ){
        Long userId = userDetails.getUser().getId();

        return notificationService.connect(userId);
    }

    @GetMapping("/modal/alarm.html")
    @PreAuthorize("isAuthenticated()")
    public String alarmPopup(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model
    ){
        Long userId = userDetails.getUser().getId();
        List<Notification> notificationList = notificationService.findAllByUserId(userId);
        model.addAttribute("notificationList", notificationList);

        return "alarm/alarm";
    }

}
