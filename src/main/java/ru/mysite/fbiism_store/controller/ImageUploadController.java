package ru.mysite.fbiism_store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.ProductImage;
import ru.mysite.fbiism_store.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    private static final String UPLOAD_DIR = "C:\\Users\\User\\IdeaProjects\\fbiism_store\\uploads\\images\\";
    private final ProductRepository productRepository;

    @Autowired
    public ImageUploadController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/upload")
    public List<String> uploadImages(@RequestParam("files") MultipartFile[] files, @RequestParam("productId") Long productId, @RequestParam("color") String color) {
        List<String> uploadedUrls = new ArrayList<>();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }

        long imageCount = product.getImages().stream().filter(image -> image.getColor().equalsIgnoreCase(color)).count();

        for (MultipartFile file : files) {
            try {
                long nextImageNumber = ++imageCount;

                String fileName = String.format("product%d_color%s_image%d.png", productId, color.toLowerCase(), nextImageNumber);

                File destinationFile = new File(UPLOAD_DIR + fileName);
                file.transferTo(destinationFile);

                String fileUrl = "http://localhost:8080/uploads/images/" + fileName;

                uploadedUrls.add(fileUrl);

                ProductImage productImage = new ProductImage();
                productImage.setUrl(fileUrl);
                productImage.setColor(color.toLowerCase());
                productImage.setProduct(product);
                product.addImage(productImage);

            } catch (IOException e) {
                e.printStackTrace();
                return List.of("Ошибка загрузки файла: " + e.getMessage());
            }
        }

        productRepository.save(product);

        return uploadedUrls;
    }

    @PutMapping("/products/{id}/images")
    public List<String> updateProductImages(@PathVariable Long id, @RequestParam("files") MultipartFile[] files, @RequestParam("color") String color) {
        List<String> uploadedUrls = new ArrayList<>();

        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return List.of("Продукт не найден");
        }

        product.getImages().removeIf(image -> image.getColor().equalsIgnoreCase(color));

        long imageCount = 0;
        for (MultipartFile file : files) {
            try {
                long nextImageNumber = ++imageCount;

                String fileName = String.format("product%d_color%s_image%d.png", id, color.toLowerCase(), nextImageNumber);

                File destinationFile = new File(UPLOAD_DIR + fileName);
                file.transferTo(destinationFile);

                String fileUrl = "http://localhost:8080/uploads/images/" + fileName;

                uploadedUrls.add(fileUrl);

                ProductImage productImage = new ProductImage();
                productImage.setUrl(fileUrl);
                productImage.setColor(color.toLowerCase());
                productImage.setProduct(product);
                product.addImage(productImage);

            } catch (IOException e) {
                e.printStackTrace();
                return List.of("Ошибка загрузки файла: " + e.getMessage());
            }
        }

        productRepository.save(product);

        return uploadedUrls;
    }
}