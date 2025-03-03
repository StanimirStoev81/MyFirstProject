package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;
import org.example.myfirstproject.Models.Enums.RoleEnum;

import java.util.List;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // Криптирана парола

    @Column(nullable = false)
    private String firstName; // Собствено име

    @Column(nullable = false)
    private String lastName; // Фамилия

    @Column(nullable = false, unique = true)
    private String email; // Имейл (уникален)

    @Column(nullable = false, unique = true)
    private String phoneNumber; // Телефонен номер

    @Column(nullable = true)
    private String address; //

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleEnum role; // USER или ADMIN

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Reservation> reservations; // Връзка към резервациите

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Payment> payments; // Връзка към плащанията

    public User() {
    }

    public User(String username, String password, String firstName, String lastName, String email, String phoneNumber, String address, RoleEnum role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }
}


