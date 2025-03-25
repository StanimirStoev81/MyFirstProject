package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "offerings")
public class Offering extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToMany(mappedBy = "offerings")
    private Set<Reservation> reservations = new HashSet<>();

    public Offering() {
    }

    public Offering(String name, String description, BigDecimal price, Set<Reservation> reservations) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.reservations = reservations;
    }

    public Offering(String name, BigDecimal price,String description) {

        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
}
