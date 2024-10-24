package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.Product;

@Component
public class OrderValidator {

    public void validateOrder(Order order) {
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
}
