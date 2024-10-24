package ru.mysite.fbiism_store.service;

import ru.mysite.fbiism_store.model.Order;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    List<Order> getAllOrders();
    Order createOrder(Order order);
    Optional<Order> getOrderById(Long id);
    Order updateOrder(Long id, Order updatedOrder);
    void deleteOrder(Long id);
    boolean existsById(Long id);
}
