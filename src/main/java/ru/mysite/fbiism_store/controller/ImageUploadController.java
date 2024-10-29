package ru.mysite.fbiism_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.service.IImageService;
import ru.mysite.fbiism_store.service.IProductService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private final IProductService productService;
    private final IImageService imageService;

    @Autowired
    public ImageUploadController(IProductService productService, IImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files,
                                                     @RequestParam("productId") Long productId,
                                                     @RequestParam("color") String color) {
        Product product = productService.getProductById(productId)
                .orElseThrow(() -> new NoSuchElementException("Продукт с ID " + productId + " не найден"));
        List<String> uploadedUrls = imageService.uploadImages(files, product, color);
        return ResponseEntity.ok(uploadedUrls);
    }

    @PutMapping("/products/{id}/images")
    public ResponseEntity<List<String>> updateProductImages(@PathVariable Long id,
                                                            @RequestParam("files") MultipartFile[] files,
                                                            @RequestParam("color") String color) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт с ID " + id + " не найден"));
        List<String> updatedUrls = imageService.updateProductImages(files, product, color);
        return ResponseEntity.ok(updatedUrls);
    }

    @GetMapping("/products/{id}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new NoSuchElementException("Продукт с ID " + id + " не найден"));
        List<String> imageUrls = imageService.getImageUrlsByProduct(product);
        return ResponseEntity.ok(imageUrls);
    }
}
