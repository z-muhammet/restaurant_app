package com.example.restaurant.service;

import com.example.restaurant.model.Role;
import com.example.restaurant.model.User;
import com.example.restaurant.repository.UserRepository;
import com.example.restaurant.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Yeni kullanıcı kaydı yapma
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayıtlı.");
        }
        // Şifreleme işlemi kaldırıldı
        return userRepository.save(user);
    }

    // Kullanıcı adını kullanarak kullanıcıyı bulma
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Yeni kullanıcı oluşturma
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayıtlı.");
        }
        // Şifreleme işlemi kaldırıldı
        return userRepository.save(user);
    }

    // Mevcut kullanıcıyı güncelleme
    public User updateUser(Long id, User userDetails) {
        User existingUser = getUserById(id);
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        if (!userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(userDetails.getPassword()); // Şifre güncelleniyor, şifreleme yok
        }
        return userRepository.save(existingUser);
    }

    // Kullanıcı silme
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    // Geçerli kullanıcıyı getirme (örneğin, kimlik doğrulama sonrası)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(
                            () -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + userDetails.getUsername()));
        } else {
            throw new SecurityException("Geçerli kullanıcı bulunamadı.");
        }
    }

    // Kullanıcı rolünü güncelleme
    public User updateUserRole(Long id, String role) {
        User user = getUserById(id);
        Optional<Role> newRole = roleRepository.findByName(role);
        if (newRole.isPresent()) {
            user.getRoles().clear();
            user.getRoles().add(newRole.get());
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Geçersiz rol: " + role);
        }
    }

    // ID ile kullanıcıyı getirme
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: ID = " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
