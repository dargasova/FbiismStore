package ru.mysite.fbiism_store.controller;

import org.springframework.web.bind.annotation.*;
import ru.mysite.fbiism_store.model.Order;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final List<Order> orders = new ArrayList<>();

    @GetMapping
    public List<Order> getAllOrders() {
        return orders;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        orders.add(order);
        return order;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        for (Order order : orders) {
            if (order.getId().equals(id)) {
                order.setProduct(updatedOrder.getProduct());
                order.setQuantity(updatedOrder.getQuantity());
                order.setCustomerDetails(updatedOrder.getCustomerDetails());
                return order;
            }
        }
        return null;
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orders.removeIf(order -> order.getId().equals(id));
        return "Заказ с id " + id + " был удален";
    }
}
