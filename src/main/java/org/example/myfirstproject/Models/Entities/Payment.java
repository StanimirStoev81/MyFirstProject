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
}
