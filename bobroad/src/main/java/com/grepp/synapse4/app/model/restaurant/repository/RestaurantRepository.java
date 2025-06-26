package com.grepp.synapse4.app.model.restaurant.repository;

import com.grepp.synapse4.app.model.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // todo 중복 데이터면 init 처리 x
    boolean existsByNameAndLatitudeAndLongitude(String name, Double latitude, Double longitude);

    List<Restaurant> findByActivatedFalse();

    List<Restaurant> findByNameContainingAndActivatedIsTrue(String restaurantKeyword);

    @Query("select r from Restaurant r left join fetch r.menus where r.id = :id")
    Optional<Restaurant> findWithMenusById(@Param("id")Long id);


    List<Restaurant> findTop10ByActivatedIsTrueOrderByIdDesc();

    @Query("SELECT r.publicId FROM Restaurant r")
    List<String> findAllPublicIds();
}
