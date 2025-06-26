package com.grepp.synapse4.app.model.notification.repository;

import com.grepp.synapse4.app.model.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        SELECT n FROM Notification n
        LEFT JOIN FETCH n.meeting
        LEFT JOIN FETCH n.vote
        WHERE n.userId = :userId
    """)
    List<Notification> findByUserId(Long userId);

    void deleteById(Long notiId);

    void deleteByUserIdAndVoteId(Long userId, Long voteId);
}
