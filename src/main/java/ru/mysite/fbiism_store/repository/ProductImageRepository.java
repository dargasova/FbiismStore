package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}