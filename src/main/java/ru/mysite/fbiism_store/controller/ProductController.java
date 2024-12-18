package ru.mysite.fbiism_store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.service.IImageService;
import ru.mysite.fbiism_store.service.IProductService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final IProductService productService;
    private final IImageService imageService;

    public ProductController(IProductService productService, IImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NoSuchElementException("Продукт не найден"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(id, updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Продукт с id " + id + " был удален");
    }

    @PostMapping("/{id}/update-images")
    public ResponseEntity<String> updateImages(@PathVariable Long id,
                                               @RequestParam("files") MultipartFile[] files,
                                               @RequestParam("color") String color) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт не найден"));
        imageService.updateProductImages(files, product, color);
        return ResponseEntity.ok("Изображения обновлены");
    }
}
