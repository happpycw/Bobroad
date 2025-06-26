package com.grepp.synapse4.app.model.llm.repository;

import com.grepp.synapse4.app.model.llm.dto.AdminCurationResultDto;
import com.grepp.synapse4.app.model.llm.dto.AdminSearchCurationDto;
import com.grepp.synapse4.app.model.llm.entity.CurationResult;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurationResultRepository extends JpaRepository<CurationResult, Long> {

    @Query("""
      SELECT new com.grepp.synapse4.app.model.llm.dto.AdminCurationResultDto(
        cr.id,
        c.title,
        r.name,
        r.address,
        cr.active,
              cr.createdAt
      )
      FROM CurationResult cr
      JOIN cr.curation c
      JOIN cr.restaurant r
      """)
    List<AdminCurationResultDto> findResultsByCurationId();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
      UPDATE CurationResult cr
      SET cr.active = CASE WHEN cr.active = true THEN false ELSE true END
      WHERE cr.id = :id
    """)
    int toggleActiveById(@Param("id") Long id);

    List<CurationResult> findAll();

        @Query("SELECT new com.grepp.synapse4.app.model.llm.dto.AdminSearchCurationDto( " +
            "  cr.id," +
            "  c.title," +
            "  r.id," +
            "  r.name," +
            "  r.category," +
            "  r.address,   " +
            "  r.branch," +
            "  c.activated" +
            ") " +
            "FROM CurationResult cr " +
            "JOIN cr.curation c " +
            "JOIN cr.restaurant r " +
            "WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY c.title, r.name")
    List<AdminSearchCurationDto> findByCurationTitleContaining(@Param("keyword") String keyword);
}