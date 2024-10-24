package ru.mysite.fbiism_store.controller;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.service.impl.ImageService;
import ru.mysite.fbiism_store.service.impl.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ImageService imageService;

    public ProductController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product product = productService.updateProduct(id, updatedProduct);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productService.existsById(id)) {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Продукт с id " + id + " был удален");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/update-images")
    public ResponseEntity<String> updateImages(@PathVariable Long id,
                                               @RequestParam("files") MultipartFile[] files,
                                               @RequestParam("color") String color) {
        Product product = productService.getProductById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        imageService.updateProductImages(files, product, color);
        return ResponseEntity.ok("Изображения обновлены");
    }
}
