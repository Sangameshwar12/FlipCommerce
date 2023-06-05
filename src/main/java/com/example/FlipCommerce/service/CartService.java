package com.example.FlipCommerce.service;

import com.example.FlipCommerce.dto.requestDto.ItemRequestDto;
import com.example.FlipCommerce.dto.responseDto.CartResponseDto;
import com.example.FlipCommerce.model.Item;

public interface CartService {

    CartResponseDto addToCart(Item item, ItemRequestDto itemRequestDto);
}
