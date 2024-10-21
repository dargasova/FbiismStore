package ru.mysite.fbiism_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order createOrder(Order order) {
        validateProductOptions(order);
        return orderRepository.save(order);
    }

    private void validateProductOptions(Order order) {
        Product product = order.getProduct();

        if (product == null) {
            throw new IllegalArgumentException("Продукт не найден.");
        }

        if (order.getColor() != null && !product.getColors().contains(order.getColor())) {
            throw new IllegalArgumentException("Выбранный цвет недоступен для данного продукта.");
        }

        if (order.getSize() != null && !product.getSizes().contains(order.getSize())) {
            throw new IllegalArgumentException("Выбранный размер недоступен для данного продукта.");
        }
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    validateProductOptions(updatedOrder);

                    order.setProduct(updatedOrder.getProduct());
                    order.setQuantity(updatedOrder.getQuantity());
                    order.setCustomerDetails(updatedOrder.getCustomerDetails());  // Исправление здесь
                    order.setColor(updatedOrder.getColor());
                    order.setSize(updatedOrder.getSize());
                    return orderRepository.save(order);
                }).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }
}
