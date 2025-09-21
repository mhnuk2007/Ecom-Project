package com.learning.springecom.service;

import com.learning.springecom.model.Order;
import com.learning.springecom.model.OrderItem;
import com.learning.springecom.model.Product;
import com.learning.springecom.model.dto.OrderItemRequest;
import com.learning.springecom.model.dto.OrderItemResponse;
import com.learning.springecom.model.dto.OrderRequest;
import com.learning.springecom.model.dto.OrderResponse;
import com.learning.springecom.repo.OrderRepo;
import com.learning.springecom.repo.ProductRepo;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private VectorStore vectorStore;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerName());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalOrderAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Update stock
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            // Update product in vector store
            updateProductInVectorStore(product);

            // Create order item
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            totalOrderAmount = totalOrderAmount.add(orderItem.getTotalPrice());
        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        // Save order in vector store
        addOrderToVectorStore(savedOrder, totalOrderAmount);

        // Prepare response
        List<OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : savedOrder.getOrderItems()) {
            itemResponses.add(new OrderItemResponse(
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getTotalPrice()
            ));
        }

        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getOrderDate(),
                itemResponses
        );
    }

    private void updateProductInVectorStore(Product product) {
        try {
            // Delete old entry
            String filter = String.format("productId == '%s'", product.getId());
            vectorStore.delete(filter);

            // Create new doc
            String productContent = String.format("""
                Product Information:
                Product ID: %d
                Name: %s
                Description: %s
                Brand: %s
                Category: %s
                Price: $%.2f
                Release Date: %s
                Available: %s
                Stock Quantity: %d
                Stock Status: %s
                """,
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getBrand(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getReleaseDate(),
                    product.isProductAvailable() ? "Yes" : "No",
                    product.getStockQuantity(),
                    product.getStockQuantity() > 10 ? "In Stock" :
                            (product.getStockQuantity() > 0 ? "Low Stock" : "Out of Stock")
            );

            Document productDoc = new Document(
                    String.valueOf(product.getId()),
                    productContent,
                    Map.of(
                            "type", "product",
                            "productId", String.valueOf(product.getId()),
                            "productName", product.getName(),
                            "category", product.getCategory()
                    )
            );

            vectorStore.add(List.of(productDoc));
            System.out.println("[ORDER SERVICE] Product updated in vector store: " + product.getName());

        } catch (Exception e) {
            System.err.println("[ORDER SERVICE] Failed to update product in vector store: " + e.getMessage());
        }
    }

    private void addOrderToVectorStore(Order order, BigDecimal totalAmount) {
        try {
            // Delete old entry
            String filter = String.format("orderId == '%s'", order.getOrderId());
            vectorStore.delete(filter);

            // Create new doc
            StringBuilder content = new StringBuilder();
            content.append("Order ID: ").append(order.getOrderId()).append("\n")
                    .append("Customer: ").append(order.getCustomerName()).append("\n")
                    .append("Email: ").append(order.getEmail()).append("\n")
                    .append("Date: ").append(order.getOrderDate()).append("\n")
                    .append("Status: ").append(order.getStatus()).append("\n")
                    .append("Total: $").append(totalAmount).append("\n");

            for (OrderItem item : order.getOrderItems()) {
                content.append("Product: ").append(item.getProduct().getName())
                        .append(", Qty: ").append(item.getQuantity())
                        .append(", Total: $").append(item.getTotalPrice()).append("\n");
            }

            Document orderDoc = new Document(
                    order.getOrderId(),
                    content.toString(),
                    Map.of(
                            "type", "order",
                            "orderId", order.getOrderId(),
                            "customerName", order.getCustomerName(),
                            "status", order.getStatus()
                    )
            );

            vectorStore.add(List.of(orderDoc));
            System.out.println("[ORDER SERVICE] Order added to vector store: " + order.getOrderId());

        } catch (Exception e) {
            System.err.println("[ORDER SERVICE] Failed to add order to vector store: " + e.getMessage());
        }
    }

    @Transactional
    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> items = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                items.add(new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getTotalPrice()
                ));
            }

            responses.add(new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    items
            ));
        }
        return responses;
    }
}
