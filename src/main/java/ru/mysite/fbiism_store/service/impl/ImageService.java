package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.ProductImage;
import ru.mysite.fbiism_store.repository.ProductImageRepository;
import ru.mysite.fbiism_store.service.IFileService;
import ru.mysite.fbiism_store.service.IImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageService {

    private final ProductImageRepository productImageRepository;
    private final IFileService fileService;

    @Autowired
    public ImageService(ProductImageRepository productImageRepository, IFileService fileService) {
        this.productImageRepository = productImageRepository;
        this.fileService = fileService;
    }

    @Override
    @Transactional
    public List<String> uploadImages(MultipartFile[] files, Product product, String color) {
        List<String> uploadedUrls = new ArrayList<>();
        long imageCount = validateColorAvailability(product, color);

        for (MultipartFile file : files) {
            try {
                String fileUrl = saveAndAddImage(file, product, color, ++imageCount);
                uploadedUrls.add(fileUrl);
            } catch (IOException e) {
                return List.of("Ошибка загрузки файла: " + e.getMessage());
            }
        }

        productImageRepository.saveAll(product.getImages());
        return uploadedUrls;
    }

    @Override
    @Transactional
    public List<String> updateProductImages(MultipartFile[] files, Product product, String color) {
        product.getImages().removeIf(image -> image.getColor().equalsIgnoreCase(color));
        return uploadImages(files, product, color);
    }

    private long validateColorAvailability(Product product, String color) {
        return product.getImages().stream()
                .filter(image -> image.getColor().equalsIgnoreCase(color))
                .count();
    }

    private String saveAndAddImage(MultipartFile file, Product product, String color, long imageCount) throws IOException {
        String fileUrl = fileService.saveFile(file, product.getId(), color, imageCount);
        product.addImage(new ProductImage(fileUrl, color.toLowerCase(), product));
        return fileUrl;
    }
}
