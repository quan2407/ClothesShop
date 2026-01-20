package com.example.ClothesShop;

import com.example.ClothesShop.entity.*;
import com.example.ClothesShop.enums.ClothingSize;
import com.example.ClothesShop.enums.SkuStatus;
import com.example.ClothesShop.repository.*;
import com.example.ClothesShop.service.InventoryReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class InventoryConcurrencyTest {
    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withEnv("TZ", "UTC");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add(
                "spring.jpa.properties.hibernate.jdbc.time_zone",
                () -> "UTC"
        );
    }

    @Autowired
    InventoryReservationService inventoryReservationService;

    @Autowired
    SKURepository skuRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void twoUsersBuyLastItem_onlyOneSuccess() throws Exception {

        // ---------- ROLE ----------
        Role userRole = roleRepository.save(
                Role.builder().name("USER").build()
        );

        // ---------- USERS ----------
        Account u1 = accountRepository.save(
                Account.builder()
                        .email("u1@test.com")
                        .password("123")
                        .role(userRole)
                        .build()
        );

        Account u2 = accountRepository.save(
                Account.builder()
                        .email("u2@test.com")
                        .password("123")
                        .role(userRole)
                        .build()
        );

        // ---------- CATEGORY ----------
        Category category = categoryRepository.save(
                Category.builder()
                        .name("T-Shirt")
                        .build()
        );

        // ---------- PRODUCT ----------
        Product product = productRepository.save(
                Product.builder()
                        .name("Basic Tee")
                        .category(category)
                        .inStock(true)
                        .build()
        );

        // ---------- SKU (LAST ITEM) ----------
        SKU sku = skuRepository.saveAndFlush(
                SKU.builder()
                        .product(product)
                        .price(100)
                        .quantity(1)
                        .clothingSize(ClothingSize.M)
                        .status(SkuStatus.ACTIVE)
                        .color("BLACK")
                        .skuCode("SKU-TEST-1")
                        .build()
        );

        Long skuId = sku.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // ---------- THREAD 1 ----------
        executor.submit(() -> {
            try {
                inventoryReservationService.reserve(u1.getId(), skuId, 1);
                System.out.println("User1 success");
            } catch (Exception e) {
                System.out.println("User1 failed: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // ---------- THREAD 2 ----------
        executor.submit(() -> {
            try {
                inventoryReservationService.reserve(u2.getId(), skuId, 1);
                System.out.println("User2 success");
            } catch (Exception e) {
                System.out.println("User2 failed: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        // ---------- ASSERT ----------
        SKU after = skuRepository.findById(skuId).orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(0, after.getQuantity());
    }

}
