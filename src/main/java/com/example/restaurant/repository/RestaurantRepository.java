
package com.example.restaurant.repository;

import com.example.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
  @Query("SELECT r, AVG(rv.serviceScore + rv.tasteScore + rv.priceScore) / 3 as avgScore " +
      "FROM Restaurant r LEFT JOIN r.reviews rv GROUP BY r")
  List<Object[]> findAllWithAverageScores();
}