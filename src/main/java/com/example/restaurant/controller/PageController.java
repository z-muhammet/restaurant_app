package com.example.restaurant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.Review;

import java.util.Arrays;
import java.util.List;

@Controller

public class PageController {
  @Autowired
  private RestTemplate restTemplate;

  @RequestMapping(value = { "/home", "/" }, method = { RequestMethod.GET, RequestMethod.POST })
  public String getIndexPage(Model model) {
    // Lokanta listesi ve ortalama puanlarını almak için API çağrısı
    String url = "http://localhost:8080/api/restaurants/average-scores";
    List<Object[]> restaurantData = Arrays.asList(restTemplate.getForObject(url, Object[][].class));

    // Model'e verileri ekleyerek frontend'e gönder
    model.addAttribute("restaurants", restaurantData);
    return "index"; // index.html Thymeleaf şablonunu döndür
  }

  @RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
  public String showLoginPage() {
    return "user/login1"; // Burada "login" Thymeleaf templates altındaki "login.html" dosyasına karşılık
    // gelir
  }

  @RequestMapping(value = "/register", method = { RequestMethod.GET, RequestMethod.POST })
  public String showRegisterPage() {
    return "user/register1"; // Burada "register" Thymeleaf templates altındaki "register.html" dosyasına
    // karşılık gelir
  }

  // Lokanta Detay Sayfası
  @GetMapping("/restaurant/{id}")
  public String getRestaurantDetailPage(@PathVariable Long id, Model model) {
    // Belirli bir lokanta bilgilerini almak için API çağrısı
    String url = "http://localhost:8080/api/restaurants/get/" + id;
    Restaurant restaurant = restTemplate.getForObject(url, Restaurant.class);

    // Model'e verileri ekleyerek frontend'e gönder
    model.addAttribute("restaurant", restaurant);
    model.addAttribute("newReview", new Review()); // Form için yeni yorum nesnesi ekleniyor
    return "restaurants/details";
  }

  // Lokanta update Sayfası
  @GetMapping("/restaurant/edit/{id}")
  public String getRestaurantUpdatePage(@PathVariable Long id, Model model) {
    // Belirli bir lokanta bilgilerini almak için API çağrısı
    String url = "http://localhost:8080/api/restaurants/get/" + id;
    Restaurant restaurant = restTemplate.getForObject(url, Restaurant.class);

    // Model'e verileri ekleyerek frontend'e gönder
    model.addAttribute("restaurant", restaurant);
    return "restaurants/update-restaurant";
  }

  // Yeni Lokanta Ekleme Sayfası
  @GetMapping("/restaurant/new")
  public String getAddRestaurantPage(Model model) {
    model.addAttribute("restaurant", new Restaurant()); // Form için yeni lokanta nesnesi ekleniyor
    return "restaurants/add-restaurant"; // add-restaurant.html Thymeleaf şablonunu döndür
  }

  @RequestMapping(value = "/add-restaurant", method = { RequestMethod.GET, RequestMethod.POST })
  public String requestMethodName(@RequestParam String param) {
    return new String();
  }

  public String addRestaurant(
      @RequestParam("name") String name,
      @RequestParam("type") String type,
      @RequestParam("address") String address,
      @RequestParam("photoUrl") String photoUrl,
      Model model) {

    // Restoran nesnesi oluştur
    Restaurant restaurant = new Restaurant();
    restaurant.setName(name);
    restaurant.setType(type);
    restaurant.setAddress(address);
    restaurant.setPhotoUrl(photoUrl);

    // JSON formatında POST isteği yap
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Restaurant> request = new HttpEntity<>(restaurant, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/api/restaurants/add", request,
          String.class);
      if (response.getStatusCode() == HttpStatus.OK) {
        model.addAttribute("message", "Restoran başarıyla eklendi!");
      } else {
        model.addAttribute("message", "Restoran eklenirken bir hata oluştu: " + response.getBody());
      }
    } catch (Exception e) {
      model.addAttribute("message", "Restoran eklenirken bir hata oluştu: " + e.getMessage());
    }

    return "restaurants/add-restaurant"; // Aynı form sayfasına geri dön
  }
}
