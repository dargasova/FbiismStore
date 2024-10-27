package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.OrderItem;
import ru.mysite.fbiism_store.model.Product;

@Component
public class OrderValidator {

    public void validateOrder(Order order) {
        // Проверяем, что есть элементы в заказе
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Заказ должен содержать хотя бы один продукт.");
        }

        // Проверяем доступность для каждого продукта в заказе
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product == null) {
                throw new IllegalArgumentException("Продукт не найден.");
            }

            // Если у продукта нет доступных размеров, устанавливаем "ONE SIZE"
            if (product.getSizes().isEmpty()) {
                item.setSize("ONE SIZE");
            } else if (item.getSize() != null && !product.getSizes().contains(item.getSize())) {
                throw new IllegalArgumentException("Выбранный размер недоступен для продукта: " + product.getName());
            }

            // Проверяем доступность цвета
            if (item.getColor() != null && !product.getColors().contains(item.getColor())) {
                throw new IllegalArgumentException("Выбранный цвет недоступен для продукта: " + product.getName());
            }
        }
    }
}
