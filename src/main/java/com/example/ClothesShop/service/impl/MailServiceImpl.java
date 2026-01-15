package com.example.ClothesShop.service.impl;

import com.example.ClothesShop.entity.Orders;
import com.example.ClothesShop.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    @Override
    public void sendOrderTrackingMail(String to, Orders order) {
        String link = "http://localhost:8080/api/v1/order/tracking/" + order.getTrackingOrder();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Xác nhận đơn hàng #" + order.getTrackingOrder());
        message.setText("""
                Cảm ơn bạn đã mua hàng!

                Mã đơn hàng: %s
                Trạng thái: %s

                Theo dõi đơn hàng tại:
                %s
                """.formatted(
                order.getTrackingOrder(),
                order.getOrderStatus(),
                link
        ));

        mailSender.send(message);
    }
}
