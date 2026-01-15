package com.example.ClothesShop.config;

import com.example.ClothesShop.entity.*;
import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.enums.SkuStatus;
import com.example.ClothesShop.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer {
    @Bean
    @Order(1)
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

    @Bean
    @Order(2)
    @Transactional
    CommandLineRunner initData(RoleRepository roleRepo,
                               CategoryRepository categoryRepo,
                               ProductRepository productRepo,
                               SKURepository skuRepo,
                               AccountRepository accountRepo,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            // 1️⃣ Init Categories
            if (categoryRepo.count() == 0) {
                List<Category> categories = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    Category cat = Category.builder().name("Category " + i).build();
                    categories.add(categoryRepo.save(cat));
                }
                categoryRepo.flush();
            }

            // 2️⃣ Init Products + SKUs
            if (productRepo.count() == 0) {
                List<Category> categories = categoryRepo.findAll();
                Random random = new Random();

                for (int i = 1; i <= 100; i++) {
                    Category cat = categories.get((i - 1) % categories.size());

                    Product product = Product.builder()
                            .name("Product " + i)
                            .description("Description for Product " + i)
                            .category(cat)
                            .build();

                    product = productRepo.saveAndFlush(product); // flush ngay để có ID

                    int skuCount;
                    if (i == 1 || i == 3 || i == 4) {
                        skuCount = 1;
                    } else {
                        skuCount = random.nextInt(20) + 1;
                    }

                    List<SKU> skus = new ArrayList<>();
                    for (int j = 1; j <= skuCount; j++) {
                        SKU sku = SKU.builder()
                                .skuCode("SKU-" + i + "-" + j)
                                .price(10 + random.nextInt(90))
                                .clothingSize(ClothingSize.values()[random.nextInt(ClothingSize.values().length)])
                                .status(SkuStatus.ACTIVE)
                                .color("Color " + ((j % 5) + 1))
                                .quantity(random.nextInt(50) + 1) // quantity >0
                                .product(product)
                                .build();
                        skus.add(sku);
                    }
                    skuRepo.saveAll(skus);
                    skuRepo.flush();

                    // 2️⃣a Update inStock
                    boolean hasStock = skus.stream().anyMatch(sku -> sku.getQuantity() > 0);
                    product.setInStock(hasStock);
                    productRepo.saveAndFlush(product);
                }
            }

            // 3️⃣ Init Accounts
            if (accountRepo.count() == 0) {
                Role staffRole = roleRepo.findByName("ROLE_STAFF").orElseThrow();
                Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseThrow();

                Account staff1 = Account.builder()
                        .email("staff1@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(staffRole)
                        .build();

                Account staff2 = Account.builder()
                        .email("staff2@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(staffRole)
                        .build();

                Account admin = Account.builder()
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("123456"))
                        .role(adminRole)
                        .build();

                accountRepo.saveAll(List.of(staff1, staff2, admin));
            }
        };
    }
}
