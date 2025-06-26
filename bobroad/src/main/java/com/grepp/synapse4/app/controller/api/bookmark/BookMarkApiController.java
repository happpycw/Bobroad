package com.grepp.synapse4.app.controller.api.bookmark;

import com.grepp.synapse4.app.model.user.BookmarkService;
import com.grepp.synapse4.app.model.user.dto.BookMarkDto;
import com.grepp.synapse4.app.model.user.dto.CustomUserDetails;
import com.grepp.synapse4.app.model.user.entity.Bookmark;
import com.grepp.synapse4.infra.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/bookmarks")
@RequiredArgsConstructor
public class BookMarkApiController {

    private final BookmarkService bookmarkService;

    @PostMapping("/toggle/{restaurantId}")
    public ResponseEntity<Map<String, Boolean>> toggleBookmark(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long restaurantId
    ) {
        Long userId = userDetails.getUser().getId();
        boolean added = bookmarkService.toggleBookmark(userId, restaurantId);
        return ResponseEntity.ok(Collections.singletonMap("added", added));
    }

    @GetMapping("/member/{userId}")
    public ResponseEntity<ApiResponse<List<BookMarkDto>>> getUserBookmark(
            @PathVariable("userId") Long userId) {
        List<BookMarkDto> dtoList = bookmarkService.getUserBookmarks(userId);
        return ResponseEntity.ok(ApiResponse.success(dtoList));
    }

    @GetMapping("/member/{userId}/ids")
    public ResponseEntity<ApiResponse<List<Long>>> getUserBookmarks(
            @PathVariable Long userId){
        List<Long> ids = bookmarkService.getUserBookmarksIds(userId);
    return ResponseEntity.ok(ApiResponse.success(ids));
    }
}