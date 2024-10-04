package ru.mysite.fbiism_store.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private String phone;

    public void setPhone(String phone) {
        if (isValidPhone(phone)) {
            this.phone = phone;
        } else {
            throw new IllegalArgumentException("Номер телефона должен быть в формате +7 (ххх) ххх-хх-хх");
        }
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}");
    }
}