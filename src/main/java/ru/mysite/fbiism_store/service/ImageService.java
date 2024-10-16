package ru.mysite.fbiism_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.ProductImage;
import ru.mysite.fbiism_store.repository.ProductImageRepository;
import ru.mysite.fbiism_store.repository.ProductRepository;

import java.io.File;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ImageService(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    public void addImagesToExistingProducts() {
        File folder = new File("uploads/images");
        if (!folder.exists() || !folder.isDirectory()) {
            logger.warn("Папка с изображениями не найдена: {}", folder.getAbsolutePath());
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            logger.info("Нет изображений для обработки в папке: {}", folder.getAbsolutePath());
            return;
        }

        for (File file : files) {
            String fileName = file.getName();
            Long productId = getProductIdFromFileName(fileName);
            String color = getColorFromFileName(fileName);
            String imageUrl = "http://localhost:8080/uploads/images/" + fileName;

            Product product = productRepository.findById(productId).orElse(null);
            if (product != null) {
                ProductImage productImage = new ProductImage(imageUrl, color, product);
                product.addImage(productImage);
                productImageRepository.save(productImage);

                logger.info("Изображение добавлено к продукту с ID {}: {}", productId, imageUrl);
            } else {
                logger.warn("Продукт с ID {} не найден для изображения {}", productId, imageUrl);
            }
        }
    }

    @Transactional
    public void updateProductImages() {
        addImagesToExistingProducts();
    }

    private Long getProductIdFromFileName(String fileName) {
        String idPart = fileName.split("_")[0].replace("product", "");
        return Long.valueOf(idPart);
    }

    private String getColorFromFileName(String fileName) {
        String[] parts = fileName.split("_");
        return parts.length > 1 ? parts[1].replace("color", "") : "default";
    }
}
