package com.grepp.synapse4.app.model.meeting;

import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.user.entity.User;
import com.grepp.synapse4.app.model.vote.dto.VoteDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.vote.entity.Vote;
import com.grepp.synapse4.app.model.vote.entity.VoteMember;
import com.grepp.synapse4.app.model.meeting.repository.MeetingMemberRepository;
import com.grepp.synapse4.app.model.meeting.repository.MeetingRepository;
import com.grepp.synapse4.app.model.vote.repository.VoteMemberRepository;
import com.grepp.synapse4.app.model.vote.repository.VoteRepository;
import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import com.grepp.synapse4.app.model.restaurant.repository.RestaurantRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {

  private final VoteRepository voteRepository;
  private final VoteMemberRepository voteMemberRepository;
  private final MeetingRepository meetingRepository;
  private final MeetingMemberRepository meetingMemberRepository;
  private final RestaurantRepository restaurantRepository;

  // 투표 등록
  @Transactional
  public Vote registVote(VoteDto dto) {
    Vote vote = new Vote();
    Meeting meeting = meetingRepository.findById(dto.getMeetingId())
        .orElseThrow(() -> new RuntimeException("모임을 찾지 못했습니다"));
    Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
        .orElseThrow(() -> new RuntimeException("식당을 찾지 못했습니다."));

    vote.setTitle(dto.getTitle());
    vote.setDescription(dto.getDescription());
    vote.setMeetingDate(dto.getMeetingDate());
    vote.setEndedAt(dto.getEndedAt());
    vote.setIsDutch(dto.getIsDutch());
    vote.setMeeting(meeting);
    vote.setRestaurant(restaurant);

    return voteRepository.save(vote);
  }

  // 투표 멤버 추가
  @Transactional
  public List<VoteMember> registVoteMember(Vote vote, Long meetingId) {
    List<MeetingMember> memberList = meetingMemberRepository.findAllByMeetingIdAndState(meetingId, State.ACCEPT);
    List<VoteMember> voteMemberList = new ArrayList<>();

    for(MeetingMember member:memberList){
      VoteMember voteMember = new VoteMember();
      voteMember.setUser(member.getUser());
      voteMember.setVote(vote);

      voteMemberRepository.save(voteMember);
      voteMemberList.add(voteMember);
    }

    return voteMemberList;
  }

  // 해당 모임의 진행중인 투표 리스트 불러오기
  @Transactional(readOnly = true)
  public List<Vote> findUpcomingVotesByMeetingId(Long meetingId) {
    return voteRepository.findUpcomingVotesByMeetingId(meetingId, LocalDateTime.now());
  }

  // 해당 모임의 종료된 투표 리스트 불러오기
  @Transactional(readOnly = true)
  public List<Vote> findPastVotesByMeetingId(Long meetingId) {
    return voteRepository.findPastVotesByMeetingId(meetingId, LocalDateTime.now());
  }

  // 유저의 투표 알림 리스트 불러오기
  @Transactional(readOnly = true)
  public List<VoteMember> findVoteListByUserId(Long userId) {
    List<VoteMember> memberList = voteMemberRepository.findAllByUserIdAndIsVoted(userId, false);

    List<VoteMember> filteredList = memberList.stream()
        .filter(member -> {
          LocalDateTime endedAt = member.getVote().getEndedAt();
          boolean isBeforeNow = endedAt.isAfter(LocalDateTime.now());
          return isBeforeNow;
        })
        .toList();

    return filteredList;
  }

  // 정보 불러오기
  @Transactional(readOnly = true)
  public Vote findVoteByVoteId(Long id) {
    return voteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("투표를 찾지 못했습니다."));
  }

  // 본인의 투표값 불러오기
  @Transactional(readOnly = true)
  public Boolean findJoinedByUserId(Long voteId, Long userId) {
    return voteMemberRepository.findIsJoinedByVoteIdAndUserId(voteId, userId);
  }

  // 투표하기
  @Transactional
  public boolean vote(Long voteId, Long userId, Boolean isJoined) {
    VoteMember voteMember = voteMemberRepository.findByVoteIdAndUserId(voteId, userId);

    // 마감된 투표는 false return
    LocalDateTime endedAt = voteMember.getVote().getEndedAt();
    if (endedAt.isBefore(LocalDateTime.now())) {
      return false;
    }

    voteMember.setIsJoined(isJoined);
    voteMember.setIsVoted(true);
    voteMemberRepository.save(voteMember);

    return true;
  }

  // 투표 별 유저의 투표 상태 불러오기
  @Transactional(readOnly = true)
  public Map<Long, Boolean> isVotedByUser(List<Vote> voteList, Long userId) {
    Map<Long, Boolean> isVotedMap = new HashMap<>();

    for(Vote vote:voteList){
      VoteMember voteMember = voteMemberRepository.findByVoteIdAndUserId(vote.getId(), userId);
      if(voteMember != null){
        Boolean isVoted = voteMember.getIsVoted();

        isVotedMap.put(vote.getId(), isVoted);
      }
    }

    return isVotedMap;
  }

  // 해당 투표의 모든 멤버의 투표 O,X 결과 불러오기
  @Transactional(readOnly = true)
  public List<String> findJoinedNicknamesByVoteId(Long id, Boolean isJoined) {
    return voteMemberRepository.findNicknamesByVoteIdAndIsJoined(id, isJoined);
  }

  public void deleteByUser(User user) {
    voteMemberRepository.deleteByUser(user);
  }
}
