package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.Product;

import java.util.List;

@Component
public class ProductValidator {

    public void validateAndSetDefaults(Product product) {
        if (product.getSizes() == null || product.getSizes().isEmpty()) {
            product.setSizes(List.of("ONE SIZE"));
        }
    }
}
