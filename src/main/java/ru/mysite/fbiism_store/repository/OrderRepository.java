package ru.mysite.fbiism_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mysite.fbiism_store.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}