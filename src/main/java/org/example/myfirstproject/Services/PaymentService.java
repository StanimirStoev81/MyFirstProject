package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.PaymentDTO;
import org.example.myfirstproject.Models.Entities.Payment;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    void processPayment(Long reservationId, BigDecimal amount, String method, String username);
    List<PaymentDTO> getAllPayments();
    void pay(Long id);
    void delete(Long id);
}
