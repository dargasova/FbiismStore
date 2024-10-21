package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
}