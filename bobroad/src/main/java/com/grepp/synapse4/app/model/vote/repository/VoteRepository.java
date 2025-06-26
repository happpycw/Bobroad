package com.grepp.synapse4.app.model.vote.repository;

import com.grepp.synapse4.app.model.vote.entity.Vote;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

  List<Vote> findAllByMeetingIdOrderByMeetingDate(Long meetingId);

  @Query("SELECT v FROM Vote v WHERE v.meeting.id = :meetingId AND v.endedAt > :now ORDER BY v.endedAt ASC")
  List<Vote> findUpcomingVotesByMeetingId(@Param("meetingId") Long meetingId, @Param("now") LocalDateTime now);

  @Query("SELECT v FROM Vote v WHERE v.meeting.id = :meetingId AND v.endedAt < :now ORDER BY v.meetingDate ASC")
  List<Vote> findPastVotesByMeetingId(@Param("meetingId") Long meetingId, @Param("now") LocalDateTime now);

}
