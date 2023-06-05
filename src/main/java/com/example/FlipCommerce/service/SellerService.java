package com.example.FlipCommerce.service;

import com.example.FlipCommerce.dto.requestDto.SellerRequestDto;
import com.example.FlipCommerce.dto.responseDto.SellerResponseDto;

public interface SellerService {

    public SellerResponseDto addSeller(SellerRequestDto sellerRequestDto);
}
