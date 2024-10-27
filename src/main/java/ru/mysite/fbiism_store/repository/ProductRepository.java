package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"colors", "sizes", "images"})
    Optional<Product> findById(Long id);
}
