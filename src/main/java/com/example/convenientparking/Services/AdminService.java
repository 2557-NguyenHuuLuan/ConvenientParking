package com.example.convenientparking.Services;


import com.example.convenientparking.Entities.Role;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Repositories.RoleRepository;
import com.example.convenientparking.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public void addUserRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Lấy các vai trò hiện tại của người dùng
        Set<Role> currentRoles = user.getRoles();

        // Tìm các vai trò mới từ danh sách roleIds
        Set<Role> newRoles = new HashSet<>(roleRepository.findAllById(roleIds));

        // Thêm các vai trò mới vào danh sách hiện tại
        currentRoles.addAll(newRoles);

        // Cập nhật các vai trò của người dùng
        user.setRoles(currentRoles);
        userRepository.save(user);
    }

    public void removeUserRoles(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Lấy các vai trò hiện tại của người dùng
        Set<Role> currentRoles = user.getRoles();

        // Chỉ xóa các vai trò mà người dùng đã chọn
        Set<Role> rolesToRemove = new HashSet<>(roleRepository.findAllById(roleIds));

        // Xóa các vai trò không cần thiết
        currentRoles.removeAll(rolesToRemove);

        // Cập nhật lại các vai trò của người dùng
        user.setRoles(currentRoles);
        userRepository.save(user);
    }



    public void blockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLocked(true);
        userRepository.save(user);
    }
    public void unblockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setLocked(false);
        userRepository.save(user);
    }

}
