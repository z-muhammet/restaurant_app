
package com.example.restaurant.repository;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.Review;
import com.example.restaurant.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsByUserAndRestaurant(User user, Restaurant restaurant);

  Optional<Review> findByUserAndRestaurant(User user, Restaurant restaurant);
}