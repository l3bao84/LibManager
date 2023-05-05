package com.example.LibManager.controller;

import com.example.LibManager.config.AdminService;
import com.example.LibManager.models.Admin;
import com.example.LibManager.models.Role;
import com.example.LibManager.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "registration")
@RequiredArgsConstructor
public class RegisterController {

    private final AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public String showRegistrationForm(ModelMap modelMap) {
        modelMap.addAttribute("admin", new Admin());
        return "register";
    }

    @PostMapping
    public String adminAcountRegister(@ModelAttribute("admin")Admin admin) {
        if(adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            return "redirect:/registration?same";
        }
        adminService.register(admin);
        return "redirect:/registration?success";
    }
}
