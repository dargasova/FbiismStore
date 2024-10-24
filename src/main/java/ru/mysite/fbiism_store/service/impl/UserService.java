package ru.mysite.fbiism_store.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mysite.fbiism_store.model.User;
import ru.mysite.fbiism_store.repository.UserRepository;
import ru.mysite.fbiism_store.service.EncryptionService;
import ru.mysite.fbiism_store.service.IUserService;
import ru.mysite.fbiism_store.validation.UserValidator;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final EncryptionService encryptionService;

    @Autowired
    public UserService(UserRepository userRepository, UserValidator userValidator, EncryptionService encryptionService) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.encryptionService = encryptionService;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            try {
                user.setEmail(encryptionService.decrypt(user.getEmail()));
                user.setPhone(encryptionService.decrypt(user.getPhone()));
            } catch (Exception e) {
                e.printStackTrace(); // Обработка ошибок
            }
        });
        return users;
    }

    @Override
    public User createUser(User user) {
        userValidator.validateUser(user);
        try {
            user.setEmail(encryptionService.encrypt(user.getEmail()));
            user.setPhone(encryptionService.encrypt(user.getPhone()));
        } catch (Exception e) {
            e.printStackTrace(); // Обработка ошибок
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        userOpt.ifPresent(user -> {
            try {
                user.setEmail(encryptionService.decrypt(user.getEmail()));
                user.setPhone(encryptionService.decrypt(user.getPhone()));
            } catch (Exception e) {
                e.printStackTrace(); // Обработка ошибок
            }
        });
        return userOpt;
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    userValidator.validateUser(updatedUser);
                    try {
                        user.setName(updatedUser.getName());
                        user.setEmail(encryptionService.encrypt(updatedUser.getEmail()));
                        user.setPhone(encryptionService.encrypt(updatedUser.getPhone()));
                    } catch (Exception e) {
                        e.printStackTrace(); // Обработка ошибок
                    }
                    return userRepository.save(user);
                }).orElse(null);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
