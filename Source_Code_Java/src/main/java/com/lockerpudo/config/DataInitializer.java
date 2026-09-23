package com.lockerpudo.config;

import com.lockerpudo.domain.*;
import com.lockerpudo.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean CommandLineRunner seedAdmin(UserAccountRepository users, PasswordEncoder encoder) { return args -> {
        if (!users.existsByEmail("admin@smartlocker.local")) users.save(new UserAccount("System administrator", "admin@smartlocker.local", "0900000000", encoder.encode("Admin@123"), UserRole.ADMIN));
    }; }
}