package ru.mysite.fbiism_store.service;

import ru.mysite.fbiism_store.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getAllUsers();
    User createUser(User user);
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
    boolean existsById(Long id);
}
