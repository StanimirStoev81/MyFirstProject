package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;
import org.example.myfirstproject.Models.Enums.ReservationStatusEnum;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity{

    @Column(nullable = false)
    private LocalDateTime reservationDate; // Дата на резервацията

    @Enumerated(EnumType.STRING)
    private ReservationStatusEnum status; // PENDING, CONFIRMED, CANCELED

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кой е направил резервацията

    @ManyToMany
    @JoinTable(
            name = "reservation_services",
            joinColumns = @JoinColumn(name = "reservations_id"),
            inverseJoinColumns = @JoinColumn(name = "services_id")
    )
    private Set<Offering> services = new HashSet<>();

    public Reservation() {
    }

    public Reservation(LocalDateTime reservationDate, ReservationStatusEnum status, User user, Set<Offering> services) {
        this.reservationDate = reservationDate;
        this.status = status;
        this.user = user;
        this.services = services;
    }
}
