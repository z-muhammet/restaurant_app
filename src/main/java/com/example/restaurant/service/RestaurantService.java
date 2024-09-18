package com.example.restaurant.service;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.User;
import com.example.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    // Lokantaların ortalama puanlarıyla birlikte listele
    public List<Object[]> listRestaurantsWithAverageScores() {
        return restaurantRepository.findAllWithAverageScores();
    }

    // Lokanta kaydetme işlemi (Yalnızca SENIOR_USER ve ADMIN yetkileri için)
    public Restaurant saveRestaurant(Restaurant restaurant, User user) {
        if (userHasAuthority(user, "ROLE_SENIOR_USER") || userHasAuthority(user, "ROLE_ADMIN")) {
            return restaurantRepository.save(restaurant);
        }
        throw new SecurityException("Lokanta ekleme yetkiniz yok.");
    }

    // Kullanıcının belirli bir yetkiye sahip olup olmadığını kontrol eder
    private boolean userHasAuthority(User user, String authority) {
        return user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
    }

    // Tüm lokantaları listele
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // Belirli bir ID'ye sahip lokantayı getir
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new Error("Lokanta bulunamadı: ID = " + id));
    }

    // Belirli bir ID ile lokantayı güncelle
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant, User user) {
        if (!userHasAuthority(user, "ROLE_SENIOR_USER") && !userHasAuthority(user, "ROLE_ADMIN")) {
            throw new SecurityException("Lokanta güncelleme yetkiniz yok.");
        }
        Restaurant existingRestaurant = getRestaurantById(id);
        existingRestaurant.setName(updatedRestaurant.getName());
        existingRestaurant.setType(updatedRestaurant.getType());
        existingRestaurant.setAddress(updatedRestaurant.getAddress());
        existingRestaurant.setPhotoUrl(updatedRestaurant.getPhotoUrl());
        return restaurantRepository.save(existingRestaurant);
    }

    // Belirli bir ID'ye sahip lokantayı sil
    public void deleteRestaurant(Long id, User user) {
        if (!userHasAuthority(user, "ROLE_ADMIN")) {
            throw new SecurityException("Lokanta silme yetkiniz yok.");
        }
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }
}
