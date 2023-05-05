package com.example.LibManager.config;

import com.example.LibManager.models.Admin;
import com.example.LibManager.models.Role;
import com.example.LibManager.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder encoder;

    public void register(Admin admin) {
        admin.setRole(Role.ADMIN);
        admin.setPassword(encoder.encode(admin.getPassword()));
        adminRepository.save(admin);
    }
}
