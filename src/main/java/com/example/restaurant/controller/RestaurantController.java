package com.example.restaurant.controller;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.User;
import com.example.restaurant.service.RestaurantService;
import com.example.restaurant.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private UserService userService;

    // Tüm lokantaları ortalama puanlarıyla birlikte listele
    // GET /api/restaurants/list-average-scores
    @GetMapping("/average-scores")
    public ResponseEntity<List<Object[]>> listRestaurantsWithAverageScores() {
        List<Object[]> restaurants = restaurantService.listRestaurantsWithAverageScores();
        return ResponseEntity.ok(restaurants);
    }

    // Tüm lokantaları listele
    // GET /api/restaurants/get-all
    @GetMapping("/get-all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    // Belirli bir ID'ye sahip lokantayı getir
    // GET /api/restaurants/get-by-id/{id}
    @GetMapping("/get/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    // Yeni lokanta ekle (Yalnızca SENIOR_USER ve ADMIN yetkileri için)
    // POST /api/restaurants/add
    @PostMapping("/add")
    public ResponseEntity<?> saveRestaurant(
            @RequestBody Restaurant restaurant) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Kimlik doğrulaması başarısız. Lütfen giriş yapın.");
            }
            try {
                Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant, currentUser);
                return ResponseEntity.ok(savedRestaurant);
            } catch (SecurityException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Lokanta ekleme yetkiniz yok.");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Bir hata oluştu.");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Kimlik doğrulaması başarısız. Lütfen giriş yapın.");
        }
    }

    // Belirli bir ID ile lokantayı güncelle (Yalnızca SENIOR_USER ve ADMIN
    // yetkileri için)
    // PUT /api/restaurants/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(
            @PathVariable Long id,
            @RequestBody Restaurant updatedRestaurant) {
        User currentUser = userService.getCurrentUser();
        Restaurant updated = restaurantService.updateRestaurant(id, updatedRestaurant, currentUser);
        return ResponseEntity.ok(updated);
    }

    // Belirli bir ID'ye sahip lokantayı sil (Yalnızca ADMIN yetkisi için)
    // DELETE /api/restaurants/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        restaurantService.deleteRestaurant(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
