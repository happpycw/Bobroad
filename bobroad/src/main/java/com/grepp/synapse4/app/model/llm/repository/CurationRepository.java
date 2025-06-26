package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.entity.Curation;
import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurationRepository extends JpaRepository<Curation, Long>{


    Curation findTopByOrderByCreatedAtDesc();

    @Query("""
                SELECT c
                FROM Curation c
                JOIN FETCH c.results cr
                JOIN FETCH cr.restaurant r
                                WHERE cr.active = true 
                """)
    List<Curation> findCurationIdWithRestaurantId();

}
