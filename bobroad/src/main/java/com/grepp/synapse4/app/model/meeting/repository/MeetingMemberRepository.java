package com.grepp.synapse4.app.model.meeting.repository;

import com.grepp.synapse4.app.model.meeting.code.Role;
import com.grepp.synapse4.app.model.meeting.code.State;
import com.grepp.synapse4.app.model.meeting.dto.AdminMeetingMemberDto;
import com.grepp.synapse4.app.model.meeting.entity.Meeting;
import com.grepp.synapse4.app.model.meeting.entity.MeetingMember;
import com.grepp.synapse4.app.model.user.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingMemberRepository extends JpaRepository<MeetingMember, Long> {
    List<MeetingMember> findAllByUserIdAndState(Long userId, State state);

    List<MeetingMember> findAllByMeetingIdAndState(Long meeting_id, State state);
    // 권한 기준으로 목록 불러오기
    @Query("""
      SELECT m FROM MeetingMember m
      WHERE m.meeting.id = :meetingId
        AND m.role <> 'OWNER'
        AND m.state = 'ACCEPT'
      ORDER BY 
        CASE m.role 
          WHEN 'ADMIN' THEN 0
          WHEN 'MEMBER' THEN 1
          ELSE 2
        END
    """)
    List<MeetingMember> findAllByMeetingIdAndRole(@Param("meetingId") Long meetingId);


    Optional<MeetingMember> findByMeetingIdAndUserId(Long meetingId, Long userId);

    // 멤버의 해당 모임 Role 불러오기
    @Query("SELECT m.role FROM MeetingMember m WHERE m.meeting.id = :meetingId AND m.user.id = :userId")
    Optional<Role> findRoleByMeetingIdAndUserId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

    Boolean existsAllByMeetingIdAndUserId(Long meetingId, Long userId);

    Integer countByMeetingIdAndState(Long meetingId, State state);

    Integer countByMeetingId(Long meetingId);

    // 관리자페이지 모임멤버보기
    @Query("""
              select new com.grepp.synapse4.app.model.meeting.dto.AdminMeetingMemberDto(
                mm.id,
                mm.meeting.id,
                mm.user.nickname
              )
              from MeetingMember mm
              where mm.meeting.id = :meetingId
            """)
    List<AdminMeetingMemberDto> findUserNicknamesByMeetingId(@Param("meetingId") Long meetingId);

    List<MeetingMember> findAllByMeetingId(Long meetingId);

    List<MeetingMember> findAllByUser(User user);

    // 이게 맞나 싶지만 맞긴 함...
    Optional<MeetingMember> findTopByMeetingAndUserNotAndStateOrderByCreatedAtAsc(Meeting meeting, User user, State state);
}