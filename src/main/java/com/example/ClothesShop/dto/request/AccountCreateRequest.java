package com.example.ClothesShop.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountCreateRequest {
    private String email;
    private String password;
}
