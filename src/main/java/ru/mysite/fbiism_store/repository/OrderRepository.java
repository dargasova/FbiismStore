package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.Product;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByItems_Product(Product product);  // Изменено для поиска по продукту через OrderItem
}
