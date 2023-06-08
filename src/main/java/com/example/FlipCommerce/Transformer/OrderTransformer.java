package com.example.FlipCommerce.Transformer;

import com.example.FlipCommerce.dto.responseDto.ItemResponseDto;
import com.example.FlipCommerce.dto.responseDto.OrderResponseDto;
import com.example.FlipCommerce.model.Customer;
import com.example.FlipCommerce.model.Item;
import com.example.FlipCommerce.model.OrderEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderTransformer {

    public static OrderEntity orderRequestDtoToOrder(Customer customer, Item item){

        return OrderEntity.builder()
                .orderNo(String.valueOf(UUID.randomUUID()))
                .totalValue(item.getProduct().getPrice()*item.getRequiredQuantity())
                .items(new ArrayList<>())
                .customer(customer)
                .build();
    }

    public static OrderResponseDto orderToOrderResponseDto(OrderEntity orderEntity) {

        List<ItemResponseDto> items = new ArrayList<>();
        for(Item item : orderEntity.getItems()){
            items.add(ItemTransformer.ItemToItemResponseDto(item));
        }
        return OrderResponseDto.builder()
                .customerName(orderEntity.getCustomer().getName())
                .orderNo(orderEntity.getOrderNo())
                .orderDate(orderEntity.getOrderDate())
                .cardNo(orderEntity.getCardUsed())
                .totalValue(orderEntity.getTotalValue())
                .items(items)
                .build();
    }
}
