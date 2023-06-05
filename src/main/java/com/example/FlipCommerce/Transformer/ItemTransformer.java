package com.example.FlipCommerce.Transformer;

import com.example.FlipCommerce.dto.responseDto.ItemResponseDto;
import com.example.FlipCommerce.model.Customer;
import com.example.FlipCommerce.model.Item;
import com.example.FlipCommerce.model.Product;

public class ItemTransformer {

    public static Item itemRequestDtoToItem(Product product, Customer customer, int quantity){

        return Item.builder()
                .product(product)
                .requiredQuantity(quantity)
                .build();

    }


    public static ItemResponseDto ItemToItemResponseDto(Item item) {
        return ItemResponseDto.builder()
                .productName(item.getProduct().getName())
                .productPrice(item.getProduct().getPrice())
                .quantityAdded(item.getRequiredQuantity())
                .build();
    }
}
