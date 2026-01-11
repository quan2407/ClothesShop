package com.example.ClothesShop.config;

import com.example.ClothesShop.entity.Role;
import com.example.ClothesShop.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepo) {
        return args -> {
            if (roleRepo.findByName("ROLE_USER").isEmpty()) {
                roleRepo.save(Role.builder()
                        .name("ROLE_USER")
                        .build());
                roleRepo.save(Role.builder()
                        .name("ROLE_ADMIN")
                        .build());
                roleRepo.save(Role.builder()
                        .name("ROLE_STAFF")
                        .build());
            }
        };
    }
}
