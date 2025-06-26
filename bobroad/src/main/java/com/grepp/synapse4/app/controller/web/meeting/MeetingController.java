package com.grepp.synapse4.app.controller.web.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.meeting.MeetingRegistRequest;
import com.grepp.synapse4.app.model.meeting.MeetingService;
import com.grepp.synapse4.app.model.meeting.VoteService;
import com.grepp.synapse4.app.model.meeting.code.Purpose;
import com.grepp.synapse4.app.model.meeting.code.Role;
import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.MeetingDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.user.CustomUserDetailsService;
import com.grepp.synapse4.app.model.user.entity.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
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
public class MeetingController {

  private final MeetingService meetingService;
  private final CustomUserDetailsService customUserDetailsService;
  private final VoteService voteService;

  @GetMapping
  public String meeting(Model model){
    Long userId = customUserDetailsService.loadUserIdByAccount();

    List<Meeting> meetingList = meetingService.findMeetingsByUserId(userId);
    model.addAttribute("meetingList", meetingList);

    return "meetings/meetings";
  }

  // 모임 생성 화면
  @GetMapping("regist")
  public String regist(Model model) {
    model.addAttribute("meetingRegistRequest", new MeetingRegistRequest());
    model.addAttribute("purpose", Purpose.values());
    return "meetings/meeting-regist";
  }

  // 모임 생성하기
  @PostMapping("regist")
  public String regist(
      @Valid MeetingRegistRequest form,
      BindingResult bindingResult,
      Model model
  ){
    if(bindingResult.hasErrors()){
      model.addAttribute("purpose", Purpose.values());
      return "meetings/meeting-regist";
    }
    Long userId = customUserDetailsService.loadUserIdByAccount();

    MeetingDto dto = form.toDto(userId);
    meetingService.registMeeting(dto);

    return "redirect:/meetings";
  }

  // 모임 상세정보 (멤버, 투표)
  @GetMapping("/detail/{id}")
  public String detail(
      @PathVariable Long id,
      Model model
  ){
    Long userId = customUserDetailsService.loadUserIdByAccount();

    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute("meeting", meeting);
    Integer count = meetingService.countMemberByMeeting(id);
    model.addAttribute("count", count);
    Role role = meetingService.findRoleByMeetingIdAndUserId(id, userId);
    model.addAttribute("role", role);

    List<Vote> upcomingList = voteService.findUpcomingVotesByMeetingId(id);
    model.addAttribute("upcomingList", upcomingList);
    List<Vote> pastList = voteService.findPastVotesByMeetingId(id);
    model.addAttribute("pastList", pastList);

    if(!upcomingList.isEmpty()){ // 모임의 투표에 대한 정보
      Map<Long, Boolean> isVotedMap = voteService.isVotedByUser(upcomingList, userId);
      model.addAttribute("upcomingMap", isVotedMap);
    }
    if(!pastList.isEmpty()){ // 모임의 투표에 대한 정보
      Map<Long, Boolean> isVotedMap = voteService.isVotedByUser(pastList, userId);
      model.addAttribute("pastMap", isVotedMap);
    }

    return "meetings/meeting-detail";
  }

  // 모임 탈퇴하기
  @PostMapping("/detail/{id}")
  public String detail(@PathVariable Long id){
    Long userId = customUserDetailsService.loadUserIdByAccount();
    meetingService.leaveMeeting(id, userId);

    return "redirect:/meetings";
  }

  @GetMapping("/modal/meeting-members/{id}.html")
  @PreAuthorize("isAuthenticated()")
  public String meetingMemberListPopup(
      @PathVariable Long id,
      Model model
  ) {
    Meeting meeting = meetingService.findMeetingsById(id);
    model.addAttribute("meeting", meeting);

    Long userId = customUserDetailsService.loadUserIdByAccount();
    Role role = meetingService.findRoleByMeetingIdAndUserId(id, userId);
    model.addAttribute("role", role);

    List<User> userList = meetingService.findMemberListByMeetingId(id, State.ACCEPT);
    model.addAttribute("userList", userList);

    return "meetings/modal/meeting-members";
  }

  @GetMapping("/modal/meeting-invite/{id}.html")
  @PreAuthorize("isAuthenticated()")
  public String meetingInvite(
      @PathVariable Long id,
      Model model
  ){
    meetingService.setInviteModel(model, id, null);

    return "meetings/modal/meeting-invite";
  }

  @GetMapping("/modal/meeting-owner/{id}.html")
  @PreAuthorize("isAuthenticated()")
  public String meetingGrant(
      @PathVariable Long id,
      Model model
  ){
    List<MeetingMember> memberList = meetingService.findMemberListByMeetingId(id);
    List<Role> limitedRoles = List.of(Role.ADMIN, Role.MEMBER);

    model.addAttribute("memberList", memberList);
    model.addAttribute("roles", limitedRoles);
    model.addAttribute("id", id);

    return "meetings/modal/meeting-owner";
  }

}