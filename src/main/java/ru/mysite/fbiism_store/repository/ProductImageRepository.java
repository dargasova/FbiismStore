package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.ProductImage;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
}