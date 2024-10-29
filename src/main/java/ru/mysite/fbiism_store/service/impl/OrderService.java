package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mysite.fbiism_store.dto.OrderDTO;
import ru.mysite.fbiism_store.model.Order;
import ru.mysite.fbiism_store.repository.OrderRepository;
import ru.mysite.fbiism_store.service.EncryptionService;
import ru.mysite.fbiism_store.service.IOrderService;
import ru.mysite.fbiism_store.validation.OrderValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final EncryptionService encryptionService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, EncryptionService encryptionService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.encryptionService = encryptionService;
    }

    @Override
    public List<OrderDTO> getAllOrdersDTO() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        orderValidator.validateOrder(order);
        validateEmailAndPhone(order.getEmail(), order.getPhone());

        try {
            order.setPhone(encryptionService.encrypt(order.getPhone()));
            order.setEmail(encryptionService.encrypt(order.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования данных: " + e.getMessage(), e);
        }

        return orderRepository.save(order);
    }

    @Override
    public Optional<OrderDTO> getOrderDTOById(Long id) {
        return orderRepository.findById(id)
                .map(this::toOrderDTO);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, Order updatedOrder) {
        if (!orderRepository.existsById(id)) {
            return null;
        }

        orderValidator.validateOrder(updatedOrder);
        validateEmailAndPhone(updatedOrder.getEmail(), updatedOrder.getPhone());

        try {
            updatedOrder.setPhone(encryptionService.encrypt(updatedOrder.getPhone()));
            updatedOrder.setEmail(encryptionService.encrypt(updatedOrder.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка шифрования данных: " + e.getMessage(), e);
        }

        return orderRepository.saveAndFlush(updatedOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    private OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setCustomerDetails(order.getCustomerDetails());

        try {
            dto.setEmail(encryptionService.decrypt(order.getEmail()));
            dto.setPhone(encryptionService.decrypt(order.getPhone()));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка расшифровки данных: " + e.getMessage(), e);
        }

        dto.setItems(order.getItems());
        return dto;
    }

    private void validateEmailAndPhone(String email, String phone) {
        if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")) {
            throw new IllegalArgumentException("Неверный формат электронной почты");
        }
        if (!phone.matches("\\+?[0-9]{10,15}")) {
            throw new IllegalArgumentException("Неверный формат номера телефона");
        }
    }
}
