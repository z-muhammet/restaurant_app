package com.example.restaurant.controller;

import com.example.restaurant.model.Role;
import com.example.restaurant.model.User;
import com.example.restaurant.service.RoleService;
import com.example.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserRoleController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * Kullanıcının rolünü günceller. Sadece adminler bu endpoint'e erişebilir.
     *
     * @param id   Güncellenecek kullanıcının ID'si
     * @param role Yeni rol ismi
     * @return Güncellenmiş kullanıcı bilgileri
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getId().equals(id)) {
            return ResponseEntity.badRequest().body("Admin cannot change their own role.");
        }

        User updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Tüm rolleri listeler. Sadece adminler bu endpoint'e erişebilir.
     *
     * @return Rollerin listesi
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles/get-all")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Yeni bir rol ekler. Sadece adminler bu endpoint'e erişebilir.
     *
     * @param role Eklenmek istenen rol
     * @return Eklenen rol
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/roles/create")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        try {
            Role addedRole = roleService.addRole(role);
            return ResponseEntity.status(201).body(addedRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Hata durumunda bad request dönüyoruz
        }
    }

    /**
     * Belirli bir ID'ye sahip rolü getirir. Sadece adminler bu endpoint'e
     * erişebilir.
     *
     * @param id Rol ID
     * @return Bulunan rol
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles/get/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        try {
            Role role = roleService.getRoleById(id);
            return ResponseEntity.ok(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Hata durumunda not found dönüyoruz
        }
    }

    /**
     * Mevcut bir rolü günceller. Sadece adminler bu endpoint'e erişebilir.
     *
     * @param id          Güncellenecek rolün ID'si
     * @param updatedRole Güncellenmiş rol bilgileri
     * @return Güncellenmiş rol
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/roles/update/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role updatedRole) {
        try {
            Role role = roleService.updateRole(id, updatedRole);
            return ResponseEntity.ok(role);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Hata durumunda bad request dönüyoruz
        }
    }

    /**
     * Bir rolü siler. Sadece adminler bu endpoint'e erişebilir.
     *
     * @param id Silinecek rolün ID'si
     * @return Boş yanıt
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/roles/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build(); // Hata durumunda not found dönüyoruz
        }
    }
}
