package com.example.restaurant.service;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.Review;
import com.example.restaurant.model.User;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    /**
     * Kullanıcı tarafından bir restorana yeni bir yorum ekler veya günceller.
     * Aynı restorana zaten yorum yapılmışsa güncelleme yapılır.
     *
     * @param review Yorum bilgileri
     * @param user   Yorum yapan kullanıcı
     * @return Kaydedilen yorum
     */
    public Review addOrUpdateReview(Review review, User user) {
        review.setUser(user);

        // Restoranı bul
        Restaurant restaurant = restaurantRepository.findById(review.getRestaurant().getId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found."));

        review.setRestaurant(restaurant);

        // Aynı kullanıcı ve restorana yapılmış mevcut bir yorumu al
        Optional<Review> existingReview = reviewRepository.findByUserAndRestaurant(user, restaurant);

        if (existingReview.isPresent()) {
            // Mevcut yorumu güncelle
            Review reviewToUpdate = existingReview.get();
            reviewToUpdate.setComment(review.getComment());
            reviewToUpdate.setServiceScore(review.getServiceScore());
            reviewToUpdate.setTasteScore(review.getTasteScore());
            reviewToUpdate.setPriceScore(review.getPriceScore());
            return reviewRepository.save(reviewToUpdate);
        } else {
            // Yeni bir yorum ekle
            return reviewRepository.save(review);
        }
    }

    /**
     * Belirli bir yorumu siler.
     *
     * @param reviewId Silinecek yorumun ID'si
     * @param user     Silme işlemini yapan kullanıcı
     */
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Yorum bulunamadı."));

        // Kullanıcı ADMIN ise veya yorum sahibi ise silebilir
        if (userHasRole(user, "ROLE_ADMIN") || review.getUser().equals(user)) {
            reviewRepository.delete(review);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sadece kendi yorumlarınızı silebilirsiniz.");
        }
    }

    /**
     * Yeni bir yorum oluşturur.
     *
     * @param review Oluşturulacak yorum
     * @return Oluşturulan yorum
     */
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    /**
     * Tüm yorumları getirir.
     *
     * @return Tüm yorumların listesi
     */
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    /**
     * Belirli bir ID'ye sahip yorumu getirir.
     *
     * @param id Yorum ID
     * @return Bulunan yorum
     */
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Yorum bulunamadı: ID = " + id));
    }

    /**
     * Belirli bir ID ile yorumu günceller.
     *
     * @param id            Güncellenecek yorumun ID'si
     * @param reviewDetails Güncellenmiş yorum bilgileri
     * @param user          Güncelleme işlemini yapan kullanıcı
     * @return Güncellenmiş yorum
     */
    public Review updateReview(Long id, Review reviewDetails, User user) {
        Review existingReview = getReviewById(id);

        // Kullanıcı ADMIN ise veya yorum sahibi ise güncelleyebilir
        if (userHasRole(user, "ROLE_ADMIN") || existingReview.getUser().equals(user)) {
            existingReview.setComment(reviewDetails.getComment());
            existingReview.setServiceScore(reviewDetails.getServiceScore());
            existingReview.setTasteScore(reviewDetails.getTasteScore());
            existingReview.setPriceScore(reviewDetails.getPriceScore());

            return reviewRepository.save(existingReview);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Yalnızca kendi yorumlarınızı güncelleyebilirsiniz.");
        }
    }

    /**
     * Kullanıcının belirli bir role sahip olup olmadığını kontrol eder.
     *
     * @param user Kullanıcı
     * @param role Kontrol edilecek rol ismi
     * @return Kullanıcının belirtilen role sahip olup olmadığı
     */
    private boolean userHasRole(User user, String role) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
    }
}
