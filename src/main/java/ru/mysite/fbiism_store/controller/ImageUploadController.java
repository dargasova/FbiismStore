package ru.mysite.fbiism_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.ProductImage;
import ru.mysite.fbiism_store.repository.ProductRepository;
import ru.mysite.fbiism_store.service.impl.ImageService;

import java.util.List;
import java.util.stream.Collectors;

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

    // Метод для загрузки изображений
    @PostMapping("/upload")
    public List<String> uploadImages(@RequestParam("files") MultipartFile[] files,
                                     @RequestParam("productId") Long productId,
                                     @RequestParam("color") String color) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }
        return imageService.uploadImages(files, product, color);
    }

    // Метод для обновления изображений
    @PutMapping("/products/{id}/images")
    public List<String> updateProductImages(@PathVariable Long id,
                                            @RequestParam("files") MultipartFile[] files,
                                            @RequestParam("color") String color) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }
        return imageService.updateProductImages(files, product, color);
    }

    // Новый метод для получения списка изображений продукта
    @GetMapping("/products/{id}/images")
    public List<String> getProductImages(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Продукт не найден");
        }

        return product.getImages().stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());
    }
}
