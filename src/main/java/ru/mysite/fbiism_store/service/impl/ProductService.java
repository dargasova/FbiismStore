package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.ProductRepository;
import ru.mysite.fbiism_store.service.IProductService;
import ru.mysite.fbiism_store.validation.ProductValidator;

import java.util.List;
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
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(Product product) {
        productValidator.validateProduct(product);
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    productValidator.validateProduct(updatedProduct);
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setColors(updatedProduct.getColors());
                    product.setSizes(updatedProduct.getSizes());
                    product.setImages(updatedProduct.getImages());
                    return productRepository.save(product);
                }).orElse(null);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
