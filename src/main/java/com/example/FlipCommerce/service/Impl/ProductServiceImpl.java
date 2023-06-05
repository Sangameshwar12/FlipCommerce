package com.example.FlipCommerce.service.Impl;

import com.example.FlipCommerce.Enum.Category;
import com.example.FlipCommerce.Enum.ProductStatus;
import com.example.FlipCommerce.dto.requestDto.ProductRequestDto;
import com.example.FlipCommerce.dto.responseDto.ProductResponseDto;
import com.example.FlipCommerce.exception.SellerNotFoundEXception;
import com.example.FlipCommerce.model.Product;
import com.example.FlipCommerce.model.Seller;
import com.example.FlipCommerce.repository.ProductRepository;
import com.example.FlipCommerce.repository.SellerRepository;
import com.example.FlipCommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    SellerRepository sellerRepository;


    @Autowired
    ProductRepository productRepository;


    @Override
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) throws SellerNotFoundEXception {


        //checking if seller exist or not
        Seller seller = sellerRepository.findByEmailId(productRequestDto.getSellerEmailId());

        if(seller == null){
            throw new SellerNotFoundEXception("Email id have not been registered");
        }
        // dto to entity

        Product product = Product.builder()
                .name(productRequestDto.getProductName())
                .price(productRequestDto.getPrice())
                .quantity(productRequestDto.getQuantity())
                .category(productRequestDto.getCategory())
                .productStatus(ProductStatus.AVAILABLE)
                .build();


        product.setSeller(seller);

        seller.getProducts().add(product);

        Seller savedSeller = sellerRepository.save(seller); // save both product and seller

        Product savedProduct = savedSeller.getProducts().get(savedSeller.getProducts().size()-1);


        // prepare for response dto
        return ProductResponseDto.builder()
                .productName(savedProduct.getName())
                .productStatus(savedProduct.getProductStatus())
                .sellerName(savedProduct.getSeller().getName())
                .category(savedProduct.getCategory())
                .price(savedProduct.getPrice())
                .build();
    }

    public List<ProductResponseDto> getByCategoryAndPrice(Category category, int price) {

        List<Product> products = productRepository.findByCategoryAndPrice(category, price);

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for(Product savedProduct: products){
            ProductResponseDto productResponseDto = ProductResponseDto.builder()
                    .productName(savedProduct.getName())
                    .productStatus(savedProduct.getProductStatus())
                    .sellerName(savedProduct.getSeller().getName())
                    .category(savedProduct.getCategory())
                    .price(savedProduct.getPrice())
                    .build();

            productResponseDtos.add(productResponseDto);
        }

        return productResponseDtos;
    }
}
