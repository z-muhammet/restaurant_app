package com.example.restaurant.service;

import com.example.restaurant.model.Role;
import com.example.restaurant.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

  @Autowired
  private RoleRepository roleRepository;

  /**
   * Yeni bir rol ekler.
   *
   * @param role Eklenmek istenen rol
   * @return Eklenen rol
   */
  public Role addRole(Role role) {
    if (roleRepository.existsByName(role.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu rol zaten mevcut.");
    }
    return roleRepository.save(role);
  }

  /**
   * İsimle bir rol bulur.
   *
   * @param roleName Rol ismi
   * @return Bulunan rol
   */
  public Role getRoleByName(String roleName) {
    return roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol bulunamadı: " + roleName));
  }

  /**
   * Tüm rolleri listeler.
   *
   * @return Roller listesi
   */
  public List<Role> getAllRoles() {
    return roleRepository.findAll().stream()
        .map(this::convertToDto) // Eğer DTO kullanıyorsanız bu satırı kullanın
        .collect(Collectors.toList());
  }

  /**
   * Belirli bir ID'ye sahip rolü getirir.
   *
   * @param id Rol ID
   * @return Bulunan rol
   */
  public Role getRoleById(Long id) {
    return roleRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol bulunamadı: ID = " + id));
  }

  /**
   * Mevcut bir rolü günceller.
   *
   * @param id          Güncellenecek rolün ID'si
   * @param updatedRole Güncellenmiş rol bilgileri
   * @return Güncellenmiş rol
   */
  public Role updateRole(Long id, Role updatedRole) {
    Role existingRole = getRoleById(id);
    if (!existingRole.getName().equals(updatedRole.getName()) && roleRepository.existsByName(updatedRole.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bu isimde bir rol zaten mevcut.");
    }
    existingRole.setName(updatedRole.getName());
    return roleRepository.save(existingRole);
  }

  /**
   * Bir rolü siler.
   *
   * @param id Silinecek rolün ID'si
   */
  public void deleteRole(Long id) {
    Role role = getRoleById(id);
    roleRepository.delete(role);
  }

  /**
   * Role nesnesini RoleDTO'ya dönüştürür.
   *
   * @param role Role nesnesi
   * @return RoleDTO
   */
  private Role convertToDto(Role role) {
    Role dto = new Role();
    dto.setId(role.getId());
    dto.setName(role.getName());
    // Gerekirse ek alanları DTO'ya ekleyin
    return dto;
  }
}
