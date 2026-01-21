package com.example.ClothesShop.enums;

public enum OrderStatus {
    PENDING, // Chờ xử lý (mặc định)
    WAITING_FOR_PAYMENT,
    CONFIRMED,   // Đã xác nhận
    SHIPPING,    // Đang giao
    DELIVERED,   // Đã giao, COD đã thu tiền
    CANCELLED    // Hủy đơn
}


