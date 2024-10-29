package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.ProductRepository;
import ru.mysite.fbiism_store.service.IProductService;
import ru.mysite.fbiism_store.validation.ProductValidator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final ProductValidator productValidator;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        productValidator.validateAndSetDefaults(product);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт не найден"));
        productValidator.validateAndSetDefaults(updatedProduct);
        existingProduct.updateFrom(updatedProduct);
        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Продукт не найден");
        }
        productRepository.deleteById(id);
    }
}
