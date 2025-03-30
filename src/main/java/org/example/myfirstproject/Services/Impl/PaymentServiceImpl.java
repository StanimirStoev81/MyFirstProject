package org.example.myfirstproject.Services.Impl;

import org.example.myfirstproject.Models.Entities.Payment;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;
import org.example.myfirstproject.Repositories.PaymentRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.PaymentService;
import org.example.myfirstproject.Services.ReservationService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationService reservationService;
    private final UserRepository userRepository;
    public PaymentServiceImpl(PaymentRepository paymentRepository, ReservationService reservationService, org.example.myfirstproject.Repositories.UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationService = reservationService;
        this.userRepository = userRepository;
    }

    @Override
    public void processPayment(Long reservationId, BigDecimal amount, String method, String username) {
        Optional<Reservation> optionalReservation = reservationService.findLastReservationByUsername(username);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();

            // Намираме потребителя по username
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Създаваме ново плащане
                Payment payment = new Payment();
                payment.setReservation(reservation);
                payment.setAmount(amount);
                payment.setUser(user);  // Задаваме потребителя на плащането

                // Ако методът е "ONLINE", задаваме плащането с карта, ако е "CASH" - с кеш
                if ("ONLINE".equals(method)) {
                    payment.setMethod(PaymentMethodEnum.CARD);
                    payment.setStatus(PaymentStatusEnum.PAID);
                } else if ("CASH".equals(method)) {
                    payment.setMethod(PaymentMethodEnum.CASH);
                    payment.setStatus(PaymentStatusEnum.PENDING);
                }

                // Записваме плащането в базата
                paymentRepository.save(payment);
            }
        }
    }
}
