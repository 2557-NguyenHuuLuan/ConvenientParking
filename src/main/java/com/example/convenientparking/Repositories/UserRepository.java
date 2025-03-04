package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findAllByUsernameNot(String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'USER' " +
            "AND NOT EXISTS (SELECT r2 FROM u.roles r2 WHERE r2.name != 'USER')")
    List<User> findAllByUserRole();

}
