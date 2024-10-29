package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.OrderItem;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.ProductRepository;

import java.util.Optional;

@Component
public class OrderValidator {

    private final ProductRepository productRepository;

    public OrderValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validateOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы один продукт");
        }
        for (OrderItem item : order.getItems()) {
            validateOrderItem(item);
        }
    }

    private void validateOrderItem(OrderItem item) {
        Product product = Optional.ofNullable(item.getProduct())
                .flatMap(p -> productRepository.findById(p.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Продукт не найден. ID: " + item.getProduct().getId()));

        if (!product.getSizes().contains(item.getSize())) {
            throw new IllegalArgumentException("Выбранный размер недоступен для продукта: " + product.getName());
        }
        if (item.getColor() != null && !product.getColors().contains(item.getColor())) {
            throw new IllegalArgumentException("Выбранный цвет недоступен для продукта: " + product.getName());
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Количество должно быть положительным числом для продукта: " + product.getName());
        }
    }
}
