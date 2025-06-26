package com.grepp.synapse4.app.controller.api;

import com.grepp.synapse4.app.model.llm.CurationResultService;
import com.grepp.synapse4.app.model.llm.dto.AdminCurationResultDto;
import com.grepp.synapse4.app.model.llm.dto.AdminSearchCurationDto;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.dto.AdminMeetingDto;
import com.grepp.synapse4.app.model.meeting.dto.AdminMeetingMemberDto;
import com.grepp.synapse4.app.model.meeting.dto.AdminMeetingSearchDto;
import com.grepp.synapse4.app.model.user.BookmarkService;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.PreferService;
import com.grepp.synapse4.app.model.user.UserService;
import com.grepp.synapse4.app.model.user.dto.AdminUserSearchDto;
import com.grepp.synapse4.app.model.user.dto.BookMarkDto;
import com.grepp.synapse4.app.model.user.dto.SurveyDto;
import com.grepp.synapse4.infra.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/admin")
//@PreAuthorize("hasRole('ADMIN')")
public class AdminApiController {

    private final BookmarkService bookmarkService;
    private final PreferService preferService;
    private final MeetingService meetingService;
    private final CurationResultService curationResultService;
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/users/prefer/{userId}")
    public ResponseEntity<ApiResponse<List<SurveyDto>>> getUserPrefer(
            @PathVariable Long userId) {
        List<SurveyDto> dtoList = preferService.getUserPreferences(userId);
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @GetMapping("/users/bookmark/{userId}")
    public ResponseEntity<ApiResponse<List<BookMarkDto>>> getUserBookmark(
            @PathVariable("userId") Long userId) {
        List<BookMarkDto> dtoList = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @GetMapping("meetingmember/{meetingId}")
    public ResponseEntity<ApiResponse<List<AdminMeetingMemberDto>>> getMeetingMember(
            @PathVariable Long meetingId) {
        List<AdminMeetingMemberDto> dtos = meetingService.findAdminMeetingByUserNickname(meetingId);
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }

    @GetMapping("search/curation")
    public ResponseEntity<ApiResponse<List<AdminSearchCurationDto>>> getCurationResults(
            @RequestParam(value = "keyword", required = false) String keyword) {
        List<AdminSearchCurationDto> results = curationResultService.searchByKeyword(keyword);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("search/meeting")
    public ResponseEntity<ApiResponse<List<AdminMeetingSearchDto>>> getMeetings(
            @RequestParam(value = "title", required = false) String title){
        List<AdminMeetingSearchDto> results = meetingService.findAdminMeetingByTitle(title);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @GetMapping("search/users")
    public ResponseEntity<ApiResponse<List<AdminUserSearchDto>>> getUsers(
            @RequestParam(value = "userAccount", required = false) String userAccount) {
        List<AdminUserSearchDto> results = customUserDetailsService.findByUserAccountContaining(userAccount);
        return ResponseEntity.ok(ApiResponse.success(results));
    }

    @DeleteMapping("{meetingId}")
    public ResponseEntity<ApiResponse<Void>> deleteMeeting(@PathVariable Long meetingId) {
        meetingService.deleteAdminMeeting(meetingId);
        ApiResponse<Void> body = ApiResponse.success(null);
        return ResponseEntity.ok(body);
    }
}