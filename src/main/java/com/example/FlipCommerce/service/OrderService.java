package com.example.FlipCommerce.service;

import com.example.FlipCommerce.dto.requestDto.OrderRequestDto;
import com.example.FlipCommerce.dto.responseDto.OrderResponseDto;
import com.example.FlipCommerce.exception.*;

public interface OrderService {

    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) throws CustomerNotFoundException, ProductNotFoundException, InsufficientQuantityException, InvalidCardException, OutOfStockException;
}
