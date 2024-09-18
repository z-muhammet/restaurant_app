package com.example.restaurant.controller;

import com.example.restaurant.model.Review;
import com.example.restaurant.model.User;
import com.example.restaurant.service.ReviewService;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    /**
     * GET /reviews/get-all: Tüm yorumları listeler.
     *
     * @return Tüm yorumların listesi
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    /**
     * GET /reviews/get/{id}: Belirli bir yorumu ID ile getirir.
     *
     * @param id Yorum ID
     * @return Bulunan yorum
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        Review review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * POST /reviews/add: Yeni bir yorum ekler veya mevcut yorumu günceller.
     *
     * @param review      Yorum bilgileri
     * @param userDetails Giriş yapmış kullanıcının detayları
     * @return Eklenen veya güncellenen yorum
     */
    @PostMapping("/add")
    public ResponseEntity<Review> addOrUpdateReview(@RequestBody Review review) {
        User currentUser = userService.getCurrentUser();
        review.setUser(currentUser); // Kullanıcıyı set et
        Review savedReview = reviewService.addOrUpdateReview(review, currentUser);
        return ResponseEntity.status(201).body(savedReview);
    }

    /**
     * PUT /reviews/update/{id}: Belirli bir yorumu günceller.
     *
     * @param id            Yorum ID
     * @param reviewDetails Güncellenmiş yorum bilgileri
     * @param userDetails   Giriş yapmış kullanıcının detayları
     * @return Güncellenmiş yorum
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id,
            @RequestBody Review reviewDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser();
        Review updatedReview = reviewService.updateReview(id, reviewDetails, currentUser);
        return ResponseEntity.ok(updatedReview);
    }

    /**
     * DELETE /reviews/delete/{id}: Belirli bir yorumu siler.
     *
     * @param id          Yorum ID
     * @param userDetails Giriş yapmış kullanıcının detayları
     * @return Boş yanıt
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.getCurrentUser();
        reviewService.deleteReview(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
