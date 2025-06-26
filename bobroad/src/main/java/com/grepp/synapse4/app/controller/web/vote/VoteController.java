package com.grepp.synapse4.app.controller.web.vote;

import com.grepp.synapse4.app.controller.web.vote.payload.VoteRegistRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.VoteService;
import com.grepp.synapse4.app.model.vote.dto.VoteDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.vote.entity.VoteMember;
import com.grepp.synapse4.app.model.notification.NotificationService;
import com.grepp.synapse4.app.model.user.BookmarkService;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.entity.Bookmark;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("meetings")
@RequiredArgsConstructor
public class VoteController {

  private final CustomUserDetailsService customUserDetailsService;
  private final BookmarkService bookmarkService;
  private final VoteService voteService;
  private final MeetingService meetingService;
  private final NotificationService notificationService;

  // 투표 등록 화면
  @GetMapping("vote-regist/{id}")
  @PreAuthorize("isAuthenticated()")
  public String voteRegist(
      @PathVariable Long id,
      Model model
  ){
    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute("isDutch", meeting.getIsDutch());

    VoteRegistRequest request = new VoteRegistRequest();
    request.setMeetingId(id);
    model.addAttribute("voteRegistRequest", request);

    return "meetings/vote/vote-regist";
  }

  // 투표 등록하기
  @PostMapping("vote-regist/{id}")
  @PreAuthorize("isAuthenticated()")
  public String voteRegist(
      @Valid VoteRegistRequest form,
      BindingResult bindingResult
  ){
    if(bindingResult.hasErrors()){
      return "meetings/vote/vote-regist";
    }
    VoteDto dto = form.toDto();
    Vote vote = voteService.registVote(dto);

    List<VoteMember> memberList = voteService.registVoteMember(vote, dto.getMeetingId());

    // 마감일자 전 투표일지 알림 생성 X
    LocalDateTime endedAt = vote.getEndedAt();
    if(endedAt.isAfter(LocalDateTime.now())){
      notificationService.memberNotification(memberList);
    }

    return "redirect:/meetings/vote/"+vote.getId();
  }

  @GetMapping("vote/{id}")
  @PreAuthorize("isAuthenticated()")
  public String voteDetail(
      @PathVariable Long id,
      Model model
  ){
    Long userId = customUserDetailsService.loadUserIdByAccount();

    Vote vote = voteService.findVoteByVoteId(id);
    model.addAttribute("vote", vote);
    Boolean isJoined = voteService.findJoinedByUserId(id, userId);
    model.addAttribute("isJoined", isJoined);

    return "meetings/vote/vote";
  }

  @GetMapping("vote-result/{id}")
  @PreAuthorize("isAuthenticated()")
  public String voteResult(
      @PathVariable Long id,
      Model model
  ){
    Vote vote = voteService.findVoteByVoteId(id);
    model.addAttribute("vote", vote);

    List<String> joinedList = voteService.findJoinedNicknamesByVoteId(id, true);
    model.addAttribute("joinedList", joinedList);
    model.addAttribute("joinedCount", joinedList.size());

    List<String> notJoinedList = voteService.findJoinedNicknamesByVoteId(id, false);
    model.addAttribute("notJoinedList", notJoinedList);
    model.addAttribute("notJoinedCount", notJoinedList.size());

    return "meetings/vote/vote-result";
  }

}