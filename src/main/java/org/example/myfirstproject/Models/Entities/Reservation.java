package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;
import org.example.myfirstproject.Models.Enums.ReservationStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity{

    @Column(nullable = false)
    private LocalDateTime startDate; // Дата на резервацията

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Column
    private BigDecimal totalOfferingPrice;

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum status; // PENDING, CONFIRMED, CANCELED

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кой е направил резервацията

    @ManyToMany
    @JoinTable(
            name = "reservation_offerings",
            joinColumns = @JoinColumn(name = "reservations_id"),
            inverseJoinColumns = @JoinColumn(name = "offerings_id")
    )
    private Set<Offering> offerings = new HashSet<>();

    public Reservation() {
    }

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, BigDecimal totalPrice, BigDecimal totalOfferingPrice, ReservationStatusEnum status, User user, Set<Offering> offerings) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
        this.totalOfferingPrice = totalOfferingPrice;
        this.status = status;
        this.user = user;
        this.offerings = offerings;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalOfferingPrice() {
        return totalOfferingPrice;
    }

    public void setTotalOfferingPrice(BigDecimal totalOfferingPrice) {
        this.totalOfferingPrice = totalOfferingPrice;
    }

    public ReservationStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ReservationStatusEnum status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(Set<Offering> offerings) {
        this.offerings = offerings;
    }
}
