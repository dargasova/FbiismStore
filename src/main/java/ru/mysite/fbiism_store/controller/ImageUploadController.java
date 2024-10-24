package ru.mysite.fbiism_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.repository.ProductRepository;
import ru.mysite.fbiism_store.service.impl.ImageService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private final ProductRepository productRepository;
    private final ImageService imageService;

    @Autowired
    public ImageUploadController(ProductRepository productRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public List<String> uploadImages(@RequestParam("files") MultipartFile[] files, @RequestParam("productId") Long productId, @RequestParam("color") String color) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }
        return imageService.uploadImages(files, product, color);
    }

    @PutMapping("/products/{id}/images")
    public List<String> updateProductImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files, @RequestParam("color") String color) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }
        return imageService.updateProductImages(files, product, color);
    }
}
