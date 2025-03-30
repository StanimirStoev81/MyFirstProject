package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;

import java.math.BigDecimal;

public interface PaymentService {
    void processPayment(Long reservationId, BigDecimal amount, String method, String username);
}
