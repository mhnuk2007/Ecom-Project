package com.learning.springecom.model.dto;

public record OrderItemRequest(
        int productId,
        int quantity
) {
}
