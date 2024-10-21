package ru.mysite.fbiism_store.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

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
