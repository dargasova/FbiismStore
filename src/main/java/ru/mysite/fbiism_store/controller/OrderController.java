package ru.mysite.fbiism_store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.service.impl.OrderService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        Order savedOrder = orderService.createOrder(order);
        // Возвращаем ID созданного заказа
        return ResponseEntity.ok(Collections.singletonMap("orderId", savedOrder.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        try {
            Order order = orderService.updateOrder(id, updatedOrder);
            return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error on update: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Server error while updating order: ", e);
            return ResponseEntity.status(500).body("Ошибка сервера при обновлении заказа");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        if (orderService.existsById(id)) {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Заказ с id " + id + " был удален");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
