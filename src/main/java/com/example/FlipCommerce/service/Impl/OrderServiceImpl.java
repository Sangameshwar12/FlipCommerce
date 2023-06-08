package com.example.FlipCommerce.service.Impl;

import com.example.FlipCommerce.Enum.ProductStatus;
import com.example.FlipCommerce.Transformer.ItemTransformer;
import com.example.FlipCommerce.Transformer.OrderTransformer;
import com.example.FlipCommerce.dto.requestDto.OrderRequestDto;
import com.example.FlipCommerce.dto.responseDto.OrderResponseDto;
import com.example.FlipCommerce.exception.*;
import com.example.FlipCommerce.model.*;
import com.example.FlipCommerce.repository.CardRepository;
import com.example.FlipCommerce.repository.CustomerRepository;
import com.example.FlipCommerce.repository.OrderRepository;
import com.example.FlipCommerce.repository.ProductRepository;
import com.example.FlipCommerce.service.OrderService;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired CardRepository cardRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) throws CustomerNotFoundException, ProductNotFoundException, InsufficientQuantityException, InvalidCardException, OutOfStockException {

        Customer customer = customerRepository.findByEmailId(orderRequestDto.getEmailId());
        if(customer == null){
            throw new CustomerNotFoundException("Customer does not exist please register first to order an product");
        }

        // check for product

        Optional<Product> optionalProduct = productRepository.findById(orderRequestDto.getProductId());
        if(optionalProduct.isEmpty()){
            throw new ProductNotFoundException("Product doesn't exist");
        }

        // check quantity


        Product product = optionalProduct.get();
        if(product.getQuantity() == 0){
            throw new OutOfStockException("Sorry! Product is out of stock");
        }

        if(product.getQuantity() < orderRequestDto.getRequiredQuantity()){
            throw new InsufficientQuantityException("Sorry! Quantity is not sufficient");
        }


        //check for card details
        Card card = cardRepository.findByCardNo(orderRequestDto.getCardNo());
        Date date = new Date();

        if(card == null || card.getCvv() != orderRequestDto.getCvv() || date.after(card.getValidTill())){
            throw new InvalidCardException("Sorry! You can not use this card");
        }


        int newQuantity = product.getQuantity() - orderRequestDto.getRequiredQuantity();
        product.setQuantity(newQuantity);
        product.setProductStatus(ProductStatus.OUT_OF_STOCK);

        Item item = ItemTransformer.itemRequestDtoToItem(orderRequestDto.getRequiredQuantity());
        item.setProduct(product);

        OrderEntity orderEntity = OrderTransformer.orderRequestDtoToOrder(customer, item);
        String maskedCard = generateMaskedCard(card);
        orderEntity.setCardUsed(maskedCard);
        orderEntity.getItems().add(item);
        item.setOrderEntity(orderEntity);


        // save order and item;
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        customer.getOrders().add(savedOrder);
        product.getItems().add(savedOrder.getItems().get(0));



        // prepare response dto
        OrderResponseDto orderResponseDto = OrderTransformer.orderToOrderResponseDto(savedOrder);

        return orderResponseDto;

    }
    private String generateMaskedCard(Card card){

        String maskedCardNo = "";
        String originalCardNo = card.getCardNo();
        for(int i = 0; i<originalCardNo.length() - 4; i++){
            maskedCardNo += '*';
        }
        maskedCardNo = maskedCardNo + originalCardNo.substring(originalCardNo.length()-4);

        return maskedCardNo;
    }
}
