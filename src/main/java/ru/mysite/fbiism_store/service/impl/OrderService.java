package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.OrderItem;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.OrderRepository;
import ru.mysite.fbiism_store.repository.ProductRepository;
import ru.mysite.fbiism_store.service.IOrderService;
import ru.mysite.fbiism_store.validation.OrderValidator;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderValidator orderValidator;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderValidator orderValidator) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderValidator = orderValidator;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Проверка на наличие элементов в заказе
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы один продукт.");
        }

        // Загружаем продукты и проверяем их доступность
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Продукт не найден. ID: " + item.getProduct().getId()));

            // Устанавливаем загруженный продукт в OrderItem
            item.setProduct(product);

            // Устанавливаем ссылку на заказ в OrderItem
            item.setOrder(order);

            // Проверяем доступность размера
            if (!product.getSizes().contains(item.getSize())) {
                throw new IllegalArgumentException("Выбранный размер недоступен для продукта: " + product.getName());
            }

            // Проверяем доступность цвета
            if (!product.getColors().contains(item.getColor())) {
                throw new IllegalArgumentException("Выбранный цвет недоступен для продукта: " + product.getName());
            }

            // Проверяем количество
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Количество должно быть положительным числом для продукта: " + product.getName());
            }
        }

        // Валидируем заказ
        orderValidator.validateOrder(order);

        // Сохраняем заказ в базе данных
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
                    for (OrderItem item : updatedOrder.getItems()) {
                        Product product = productRepository.findById(item.getProduct().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден. ID: " + item.getProduct().getId()));

                        item.setProduct(product);
                        item.setOrder(order); // Устанавливаем ссылку на заказ

                        // Проверки доступности размеров и цветов
                        if (!product.getSizes().contains(item.getSize())) {
                            throw new IllegalArgumentException("Выбранный размер недоступен для продукта: " + product.getName());
                        }
                        if (!product.getColors().contains(item.getColor())) {
                            throw new IllegalArgumentException("Выбранный цвет недоступен для продукта: " + product.getName());
                        }
                    }

                    orderValidator.validateOrder(updatedOrder);
                    order.setCustomerDetails(updatedOrder.getCustomerDetails());
                    order.setItems(updatedOrder.getItems());
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
