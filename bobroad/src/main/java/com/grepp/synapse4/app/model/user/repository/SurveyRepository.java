package com.grepp.synapse4.app.model.user.repository;

import com.grepp.synapse4.app.model.user.entity.Survey;
import com.grepp.synapse4.app.model.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Optional<Survey> findByUser(User user);
    Optional<Survey> findByUserId(Long userId);

}
