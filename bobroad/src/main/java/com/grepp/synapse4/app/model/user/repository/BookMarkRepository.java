package com.grepp.synapse4.app.model.user.repository;



import com.grepp.synapse4.app.model.user.dto.BookMarkDto;
import com.grepp.synapse4.app.model.user.dto.BookMarkRegistDto;
import com.grepp.synapse4.app.model.user.dto.MyBookMarkDto;
import com.grepp.synapse4.app.model.user.dto.RankingDto;
import com.grepp.synapse4.app.model.user.entity.Bookmark;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {
  List<Bookmark> findAllByUserId(Long userId);


    @Query("""
      SELECT b
      FROM Bookmark b
      JOIN FETCH b.restaurant r
      WHERE b.user.id = :userId
      ORDER BY b.createdAt DESC
    """)
    List<Bookmark> findByUserId(Long userId);

    @Query("""
      select new com.grepp.synapse4.app.model.user.dto.RankingDto(
        r.id,
        count(b),
        r.name,
        r.address,
        r.branch,
        r.category,
        rd.rowBusinessTime
      )
      from Bookmark b
      join b.restaurant r
      join r.detail rd
      group by
        r.id,
        r.name,
        r.address,
        r.branch,
        r.category,
        rd.rowBusinessTime
      order by
        count(b) desc
    """)
    List<RankingDto> findRestaurantRanking();

    @Query("""
      select new com.grepp.synapse4.app.model.user.dto.MyBookMarkDto(
        u.id,
        b.id,
        b.createdAt,
        r.id,
        r.name,
        r.address,
        r.branch,
        r.category,
        rd.rowBusinessTime
      )
      from Bookmark b
      join b.user u
      join b.restaurant r
      join r.detail rd
      where u.id = :userId
    """)
    List<MyBookMarkDto> findmybookmark(Long userId);

    Optional<Bookmark> findByUserIdAndRestaurantId(Long userId, Long restaurantId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Bookmark b WHERE b.user.id = :userId AND b.restaurant.id = :restaurantId")
    void deleteByUserIdAndRestaurantId(Long userId, Long restaurantId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO bookmark (user_id, restaurant_id, created_at) VALUES (:userId, :restaurantId, CURRENT_TIMESTAMP)", nativeQuery = true)
    int insertBookmark(Long userId, Long restaurantId);
}
