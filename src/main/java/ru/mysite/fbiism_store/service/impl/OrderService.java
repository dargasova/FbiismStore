package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.repository.OrderRepository;
import ru.mysite.fbiism_store.service.IOrderService;
import ru.mysite.fbiism_store.validation.OrderValidator;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        orderValidator.validateOrder(order);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    orderValidator.validateOrder(updatedOrder);
                    order.setProduct(updatedOrder.getProduct());
                    order.setQuantity(updatedOrder.getQuantity());
                    order.setCustomerDetails(updatedOrder.getCustomerDetails());
                    order.setColor(updatedOrder.getColor());
                    order.setSize(updatedOrder.getSize());
                    return orderRepository.save(order);
                }).orElse(null);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }
}
