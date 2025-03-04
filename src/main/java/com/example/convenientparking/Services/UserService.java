package com.example.convenientparking.Services;

import com.example.convenientparking.Constants.RoleEnum;
import com.example.convenientparking.Entities.*;
import com.example.convenientparking.Repositories.RoleRepository;
import com.example.convenientparking.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    public boolean isPhoneExists(String phone) {
        return userRepository.findByPhone(phone) != null;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setVerified(false);
        user.setLocked(false);
        user.setBalance(0L);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
    public void setDefaultRole(String username){
        userRepository.findByUsername(username).ifPresentOrElse(user -> {
                    user.getRoles().add(roleRepository.findRoleById(roleRepository.findRoleIdByRoleName(RoleEnum.USER.name())));
                    userRepository.save(user);
                },
                () -> {
                    throw new UsernameNotFoundException("User not found");
                });

    }
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(user.isLocked())
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }



    public User findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return userRepository.findByUsername(currentUserName);
    }

    public List<User> listAllUsersExceptCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userRepository.findAllByUsernameNot(currentUsername);
    }

    public boolean depositIntoAccount(User user, double amount) {
        if (amount <= 0) {
            log.error("Deposit amount must be positive. Amount: {}", amount);
            return false;
        }
        double newBalance = user.getBalance() + amount;
        user.setBalance((long) newBalance);
        userRepository.save(user);
        log.info("Deposited {} into the account of user {}. New balance: {}", amount, user.getUsername(), newBalance);
        return true;
    }
    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null; // Kiểm tra email có tồn tại không
    }

    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email); // Lấy người dùng theo email
        if (user != null) {
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword)); // Mã hóa mật khẩu mới
            userRepository.save(user);
        }

    }

    public boolean verifyPassword(User user, String rawPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public boolean verifyUser(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setVerified(true);  
            userRepository.save(user);
            return true;
        }
        return false;
    }


}
