package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);
    Optional<Role> findByName(String name);
    @Query("SELECT r.id FROM Role r WHERE r.name = :name")
    Optional<Long> findRoleIdByName(String name);

    @Query("SELECT r.id FROM Role r WHERE r.name = :name")
    Long findRoleIdByRoleName(String name);
}
