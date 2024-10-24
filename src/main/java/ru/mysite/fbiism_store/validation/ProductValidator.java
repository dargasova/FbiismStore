package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.Product;

@Component
public class ProductValidator {

    public void validateProduct(Product product) {
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым.");
        }

        if (product.getPrice() == null || product.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена продукта должна быть положительной.");
        }

        if (product.getColors() == null || product.getColors().isEmpty()) {
            throw new IllegalArgumentException("Продукт должен иметь хотя бы один доступный цвет.");
        }

        if (product.getSizes() == null || product.getSizes().isEmpty()) {
            throw new IllegalArgumentException("Продукт должен иметь хотя бы один доступный размер.");
        }
    }
}
