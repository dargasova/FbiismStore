package ru.mysite.fbiism_store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.ProductImage;
import ru.mysite.fbiism_store.repository.ProductImageRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private static final String UPLOAD_DIR = "C:\\Users\\User\\IdeaProjects\\fbiism_store\\uploads\\images\\";
    private final ProductImageRepository productImageRepository;

    @Autowired
    public ImageService(ProductImageRepository productImageRepository) {
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    public List<String> uploadImages(MultipartFile[] files, Product product, String color) {
        List<String> uploadedUrls = new ArrayList<>();
        long imageCount = product.getImages().stream()
                .filter(image -> image.getColor().equalsIgnoreCase(color))
                .count();

        for (MultipartFile file : files) {
            try {
                String fileUrl = saveFile(file, product.getId(), color, ++imageCount);
                uploadedUrls.add(fileUrl);
                product.addImage(new ProductImage(fileUrl, color.toLowerCase(), product));
            } catch (IOException e) {
                return List.of("Ошибка загрузки файла: " + e.getMessage());
            }
        }

        productImageRepository.saveAll(product.getImages());
        return uploadedUrls;
    }

    @Transactional
    public List<String> updateProductImages(MultipartFile[] files, Product product, String color) {
        product.getImages().removeIf(image -> image.getColor().equalsIgnoreCase(color));
        return uploadImages(files, product, color);
    }

    private String saveFile(MultipartFile file, Long productId, String color, long imageCount) throws IOException {
        String fileName = String.format("product%d_color%s_image%d.png", productId, color.toLowerCase(), imageCount);
        File destinationFile = new File(UPLOAD_DIR + fileName);
        file.transferTo(destinationFile);
        return "http://localhost:8080/uploads/images/" + fileName;
    }
}
