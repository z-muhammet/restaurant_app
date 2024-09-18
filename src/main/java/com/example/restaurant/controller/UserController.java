package com.example.restaurant.controller;

import com.example.restaurant.model.User;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/users/create: Yeni bir kullanıcı oluşturur.
     *
     * @param user Oluşturulacak kullanıcı bilgileri
     * @return Oluşturulan kullanıcı
     */
    @PostMapping("/create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(201).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Hata durumunda bad request dönüyoruz
        }
    }

    /**
     * GET /api/users/get-all: Tüm kullanıcıları getirir.
     *
     * @return Kullanıcıların listesi
     */
    @GetMapping("/get-all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/get/{id}: Belirli bir ID'ye sahip kullanıcıyı getirir.
     *
     * @param id Kullanıcı ID
     * @return Bulunan kullanıcı
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/users/update/{id}: Belirli bir ID ile kullanıcıyı günceller.
     *
     * @param id          Güncellenecek kullanıcı ID
     * @param userDetails Güncellenmiş kullanıcı bilgileri
     * @return Güncellenmiş kullanıcı
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Hata durumunda bad request dönüyoruz
        }
    }

    /**
     * DELETE /api/users/delete/{id}: Belirli bir kullanıcıyı siler.
     *
     * @param id Silinecek kullanıcı ID
     * @return Boş yanıt
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/users/update-role/{id}?role={role}: Belirli bir kullanıcıyı rol ile
     * günceller.
     *
     * @param id   Kullanıcı ID
     * @param role Rol ismi
     * @return Güncellenmiş kullanıcı
     */
    @PutMapping("/update-role/{id}")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        try {
            User updatedUser = userService.updateUserRole(id, role);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Hata durumunda bad request dönüyoruz
        }
    }
}
