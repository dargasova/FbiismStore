package ru.mysite.fbiism_store.service;

import org.springframework.web.multipart.MultipartFile;
import ru.mysite.fbiism_store.model.Product;

import java.util.List;

public interface IImageService {
    List<String> uploadImages(MultipartFile[] files, Product product, String color);
    List<String> updateProductImages(MultipartFile[] files, Product product, String color);
}
