package ru.mysite.fbiism_store.service;

import ru.mysite.fbiism_store.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> getAllProducts();

    Product createProduct(Product product);

    Optional<Product> getProductById(Long id);

    Product updateProduct(Long id, Product updatedProduct);

    void deleteProduct(Long id);
}