package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.dto.response.CheckoutReservationItem;
import com.example.ClothesShop.dto.response.CheckoutReservationView;
import com.example.ClothesShop.entity.Account;
import com.example.ClothesShop.entity.InventoryReservation;
import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.entity.SKU;
import com.example.ClothesShop.entity.redis.cart.Cart;
import com.example.ClothesShop.entity.redis.cart.CartItem;
import com.example.ClothesShop.enums.OrderStatus;
import com.example.ClothesShop.enums.PaymentMethod;
import com.example.ClothesShop.enums.PaymentStatus;
import com.example.ClothesShop.enums.ReservationStatus;
import com.example.ClothesShop.exception.NotFoundException;
import com.example.ClothesShop.repository.AccountRepository;
import com.example.ClothesShop.repository.InventoryReservationRepository;
import com.example.ClothesShop.repository.redis.CartRepository;
import com.example.ClothesShop.service.CheckoutService;
import com.example.ClothesShop.service.InventoryReservationService;
import com.example.ClothesShop.service.MailService;
import com.example.ClothesShop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartRepository cartRepository;
    private final InventoryReservationService inventoryReservationService;
    private final InventoryReservationRepository inventoryReservationRepository;
    private final AccountRepository accountRepository;
    private final OrderService orderService;
    private final MailService mailService;

    @Override
    @Transactional
    public void checkout(Long accountId) {
        Cart cart = cartRepository.findById(accountId).orElseThrow(() -> new NotFoundException("Cart not found"));
        if (cart.getItems().isEmpty()) {
            throw new NotFoundException("Cart is empty");
        }
        for (CartItem item : cart.getItems()) {
            inventoryReservationService.reserve(accountId, item.getSkuId(), item.getQuantity());
        }
        cartRepository.deleteById(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutReservationView view(Long accountId) {
        List<InventoryReservation> reservations =
                inventoryReservationRepository.findByAccount_IdAndStatus(accountId, ReservationStatus.HOLD);
        if (reservations.isEmpty()) {
            throw new NotFoundException("No active checkout");
        }
        double total = 0;
        List<CheckoutReservationItem> items = new ArrayList<>();
        for (InventoryReservation r : reservations) {
            SKU sku = r.getSku();
            double itemTotal = sku.getPrice() * r.getQuantity();
            total += itemTotal;

            items.add(
                    CheckoutReservationItem.builder()
                            .reservationId(r.getId())
                            .skuId(sku.getId())
                            .productName(sku.getProduct().getName())
                            .size(sku.getClothingSize().name())
                            .color(sku.getColor())
                            .price(sku.getPrice())
                            .quantity(r.getQuantity())
                            .itemTotal(itemTotal)
                            .expireAt(r.getExpireAt())
                            .build()
            );
        }
        return CheckoutReservationView.builder()
                .items(items)
                .totalAmount(total)
                .expireAt(
                        reservations.stream()
                                .map(InventoryReservation::getExpireAt)
                                .min(LocalDateTime::compareTo)
                                .orElse(null)
                )
                .build();
    }

    @Override
    @Transactional
    public void cancel(Long accountId) {
        inventoryReservationService.cancelAllHoldByAccount(accountId);
    }

    @Override
    @Transactional
    public Orders confirm(Long accountId, String address, String phoneNumber, PaymentMethod paymentMethod) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        List<InventoryReservation> reservations =
                inventoryReservationService.confirmAllByAccount(accountId);
        Orders order = orderService.createOrder(
                account,
                reservations,
                address,
                phoneNumber,
                paymentMethod
        );
        if (paymentMethod == PaymentMethod.COD) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
            order.setOrderStatus(OrderStatus.CONFIRMED);
            mailService.sendOrderTrackingMail(account.getEmail(), order);
        } else {
            order.setPaymentStatus(PaymentStatus.UNPAID);
            order.setOrderStatus(OrderStatus.WAITING_FOR_PAYMENT);
        }
        return order;
    }


}
