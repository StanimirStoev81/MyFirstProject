package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment extends BaseEntity{


    @Column(nullable = false)
    private BigDecimal amount; // Сума на плащането

    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status; // PAID, PENDING

    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum method; // CARD, CASH

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кой е направил плащането

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation; // Към коя резервация се отнася

    public Payment() {
    }

    public Payment(BigDecimal amount, PaymentStatusEnum status, PaymentMethodEnum method, User user, Reservation reservation) {
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.user = user;
        this.reservation = reservation;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentStatusEnum getStatus() {
        return status;
    }

    public void setStatus(PaymentStatusEnum status) {
        this.status = status;
    }

    public PaymentMethodEnum getMethod() {
        return method;
    }

    public void setMethod(PaymentMethodEnum method) {
        this.method = method;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
