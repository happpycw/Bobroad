package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.controller.web.meeting.payload.meeting.MeetingGrantRequest;
import com.grepp.synapse4.app.model.meeting.code.Role;
import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.*;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.meeting.repository.MeetingMemberRepository;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final ModelMapper mapper;
  private final UserRepository userRepository;
  private final MeetingMemberRepository meetingMemberRepository;


  // 관리자 모든 모임 불러오기
  @Transactional(readOnly = true)
  public List<AdminMeetingDto> findAllForAdmin() {
    return meetingRepository.findAllWithCreatorAccount();
  }

  // 관리자페이지 모임멤버보기
  @Transactional(readOnly = true)
  public List<AdminMeetingMemberDto> findAdminMeetingByUserNickname(Long meetingId) {
    return meetingMemberRepository.findUserNicknamesByMeetingId(meetingId);
  }

  // 모임 등록하기
  @Transactional
  public void registMeeting(MeetingDto dto){
    Meeting meeting = mapper.map(dto, Meeting.class);
    User user = userRepository.findById(dto.getCreatorId())
        .orElseThrow(() -> new RuntimeException("유저를 찾지 못했습니다."));
    meeting.setUser(user);
    meetingRepository.save(meeting);

    MeetingMember meetingMember = new MeetingMember();
    meetingMember.setRole(Role.OWNER);
    meetingMember.setState(State.ACCEPT);
    meetingMember.setMeeting(meeting);
    meetingMember.setUser(user);

    meetingMemberRepository.save(meetingMember);
  }

  // 본인의 모임 리스트 불러오기
  @Transactional(readOnly = true)
  public List<Meeting> findMeetingsByUserId(Long userId){
    List<MeetingMember> meetingMemberList =  meetingMemberRepository.findAllByUserIdAndState(userId, State.ACCEPT);

    return meetingMemberList.stream()
        .map(MeetingMember::getMeeting)
        .sorted(Comparator.comparing(Meeting::getCreatedAt).reversed())
        .collect(Collectors.toList());
  }

  // 모임 상세정보 불러오기
  @Transactional(readOnly = true)
  public Meeting findMeetingsById(Long id) {
    return meetingRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다."));
  }

  // 모임의 멤버 수 불러오기
  @Transactional(readOnly = true)
  public Integer countMemberByMeeting(Long id) {
    return meetingMemberRepository.countByMeetingIdAndState(id, State.ACCEPT);
  }



  // 모임 초대하기
  @Transactional
  public Meeting inviteUser(MeetingMemberDto dto) {
    MeetingMember member = new MeetingMember();
    Meeting meeting = meetingRepository.findById(dto.getMeetingId())
        .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다."));
    User user = userRepository.findById(dto.getUserId())
        .orElseThrow(() -> new RuntimeException("유저를 찾지 못했습니다."));

    member.setState(State.WAIT);
    member.setMeeting(meeting);
    member.setUser(user);

    meetingMemberRepository.save(member);

    return meeting;
  }

  // 모임 초대 수락, 거절
  @Transactional
  public void updateInvitedState(Long meetingId, Long userId, State state) {
    MeetingMember member = meetingMemberRepository.findByMeetingIdAndUserId(meetingId, userId)
        .orElseThrow(() -> new EntityNotFoundException("데이터를 찾지 못했습니다"));

    if (state.equals(State.REJECT)) {
      meetingMemberRepository.delete(member);
    } else if (state.equals(State.ACCEPT)) {
      member.setRole(Role.MEMBER);
      member.setState(State.ACCEPT);
      meetingMemberRepository.save(member);
    }
  }

  // 모임 초대 리스트 불러오기
  @Transactional(readOnly = true)
  public List<MeetingMember> findInviteByUserId(Long userId) {
    return meetingMemberRepository.findAllByUserIdAndState(userId, State.WAIT);
  }

  // 모임에 유저 포함 여부 확인
  @Transactional(readOnly = true)
  public Boolean findMemberByMeetingIdAndUserId(Long meetingId, Long userId) {
    return meetingMemberRepository.existsAllByMeetingIdAndUserId(meetingId, userId);
  }

  // 모임의 멤버 리스트 불러오기
  @Transactional(readOnly = true)
  public List<User> findMemberListByMeetingId(Long meetingId, State state) {
    List<MeetingMember> memberList = meetingMemberRepository.findAllByMeetingIdAndState(meetingId, state);

    return memberList.stream()
            .map(MeetingMember::getUser)
            .collect(Collectors.toList());
  }

  // 모임장을 제외한 멤버 리스트 불러오기
  @Transactional(readOnly = true)
  public List<MeetingMember> findMemberListByMeetingId(Long meetingId) {
    return meetingMemberRepository.findAllByMeetingIdAndRole(meetingId);
  }

  // 멤버 리스트 관련 설정
  public void setInviteModel(Model model, Long meetingId, String errorMessage) {
    List<User> invitedList = this.findMemberListByMeetingId(meetingId, State.WAIT);
    model.addAttribute("invitedList", invitedList);
    model.addAttribute("meetingId", meetingId);
    if (errorMessage != null) {
      model.addAttribute("error", errorMessage);
    }
  }

  // 관리자페이지 모임검색
  @Transactional(readOnly = true)
  public List<AdminMeetingSearchDto> findAdminMeetingByTitle(String title) {
    return meetingRepository.findByTitleContaining(title);
  }

  @Transactional
  public void deleteAdminMeeting(Long meetingId) {
    List<MeetingMember> members = meetingMemberRepository.findAllByMeetingId(meetingId);
    meetingMemberRepository.deleteAll(members);
    meetingRepository.deleteById(meetingId);
  }

  // 유저의 모임 Role 구하기
  @Transactional(readOnly = true)
  public Role findRoleByMeetingIdAndUserId(Long meetingId, Long userId) {
    return meetingMemberRepository.findRoleByMeetingIdAndUserId(meetingId, userId)
            .orElseThrow(() -> new EntityNotFoundException("데이터를 찾지 못했습니다"));
  }

  // 멤버의 Role 변경
  @Transactional
  public void updateGrantByMember(List<MeetingGrantRequest> request) {
    for(MeetingGrantRequest grant:request){
      MeetingMember member = meetingMemberRepository.findById(grant.getMemberId())
              .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다."));
      member.setRole(grant.getRole());

      meetingMemberRepository.save(member);
    }
  }

  @Transactional
  public void deleteByUser(User user) {

    // 1. meetingMem에서 user가 있는 모든 meeting id값 찾기
    List<MeetingMember> joinedMeetings = meetingMemberRepository.findAllByUser(user);

    for(MeetingMember joinedMeeting : joinedMeetings){
      Meeting meeting = joinedMeeting.getMeeting();

      meetingMemberRepository.delete(joinedMeeting);
      meetingMemberRepository.flush();

      // 2. 해당 meeting의 creatorId가 user인지 확인 후 맞다면 위임 진행
      // 멤버가 없을 시 모임 제거
      int memberCount = meetingMemberRepository.countByMeetingId(meeting.getId());
      if(memberCount == 0){
        meetingRepository.delete(meeting);
      } else {
        // meeting에서의 권한 위임 또는 meeting 삭제
        delegatingCreator(meeting, user);
      }

    }

    // 3. 모든 meeting 순회 종료 및 위임 또는 미팅 삭제 절차 종료
    // MeeetingMember에 있는 user 모두 삭제
//    if(!joinedMeetings.isEmpty()){
//      meetingMemberRepository.deleteAll(joinedMeetings);
//    }
  }

  @Transactional
  protected void delegatingCreator(Meeting meeting, User user) {
    if(meeting.getUser().getId().equals(user.getId())){
      // 맞다면 user를 제외한 가장 오래된 ACCEPT 멤버 찾기
      MeetingMember oldestMember =
              meetingMemberRepository.findTopByMeetingAndUserNotAndStateOrderByCreatedAtAsc(meeting, user, State.ACCEPT)
                      .orElseThrow(() -> new RuntimeException("해당 멤버가 없습니다."));

      // 멤버가 있다면, creatorId 위임
      User newCreator = oldestMember.getUser();
      meeting.setUser(newCreator);
      meetingRepository.saveAndFlush(meeting);

      // 권한 위임
      oldestMember.setRole(Role.OWNER);
      meetingMemberRepository.save(oldestMember);
    }
  }


  // 모임 탈퇴하기
  @Transactional
  public void leaveMeeting(Long id, Long userId) {
    MeetingMember member = meetingMemberRepository.findByMeetingIdAndUserId(id, userId)
            .orElseThrow(() -> new RuntimeException("해당 멤버가 없습니다."));

    Meeting meeting = member.getMeeting();
    User user = member.getUser();

    // meetingmember에서의 user 삭제
    meetingMemberRepository.delete(member);
    meetingMemberRepository.flush();

    // 멤버가 없을 시 모임 제거
    int memberCount = meetingMemberRepository.countByMeetingId(id);
    if(memberCount == 0){
      meetingRepository.delete(meeting);
      meetingRepository.flush();
    } else {
      // meeting에서의 권한 위임 또는 meeting 삭제
      delegatingCreator(meeting, user);
    }
  }
}