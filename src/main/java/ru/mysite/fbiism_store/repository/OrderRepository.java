package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.model.Product;
import ru.mysite.fbiism_store.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByProduct(Product product);
}