package ru.mysite.fbiism_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.service.impl.ImageService;
import ru.mysite.fbiism_store.service.impl.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private final ProductService productService;
    private final ImageService imageService;

    @Autowired
    public ImageUploadController(ProductService productService, ImageService imageService) {
        this.productService = productService;
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public List<String> uploadImages(@RequestParam("files") MultipartFile[] files,
                                     @RequestParam("productId") Long productId,
                                     @RequestParam("color") String color) {
        Product product = productService.getProductById(productId);
        return imageService.uploadImages(files, product, color);
    }

    @PutMapping("/products/{id}/images")
    public List<String> updateProductImages(@PathVariable Long id,
                                            @RequestParam("files") MultipartFile[] files,
                                            @RequestParam("color") String color) {
        Product product = productService.getProductById(id);
        return imageService.updateProductImages(files, product, color);
    }

    @GetMapping("/products/{id}/images")
    public List<String> getProductImages(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return imageService.getImageUrlsByProduct(product);
    }
}
