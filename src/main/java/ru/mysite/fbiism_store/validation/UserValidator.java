package ru.mysite.fbiism_store.validation;

import org.springframework.stereotype.Component;
import ru.mysite.fbiism_store.model.User;

@Component
public class UserValidator {

    public void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым.");
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым.");
        }

        if (!user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Некорректный формат email.");
        }
    }
}
