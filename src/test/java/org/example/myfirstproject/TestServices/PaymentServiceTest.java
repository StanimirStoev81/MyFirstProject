package org.example.myfirstproject.TestServices;

import org.example.myfirstproject.Models.DTO.PaymentDTO;
import org.example.myfirstproject.Models.Entities.Payment;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;
import org.example.myfirstproject.Repositories.PaymentRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.Impl.PaymentServiceImpl;
import org.example.myfirstproject.Services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getAllPayments_ShouldReturnPaymentList() {
        // Arrange
        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        Payment mockPayment = new Payment(new BigDecimal("100.00"), PaymentStatusEnum.PENDING, PaymentMethodEnum.CASH, mockUser, null);
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(mockPayment);

        when(paymentRepository.findAll()).thenReturn(paymentList);

        // Act
        List<PaymentDTO> result = paymentService.getAllPayments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("100.00", result.get(0).getAmount().toString());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(paymentRepository, times(1)).findAll();
    }


    @Test
    void pay_ShouldUpdatePaymentStatusToPaid() {
        // Arrange
        Payment payment = new Payment(new BigDecimal("50.00"), PaymentStatusEnum.PENDING, PaymentMethodEnum.CASH, null, null);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // Act
        paymentService.pay(1L);

        // Assert
        assertEquals(PaymentStatusEnum.PAID, payment.getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void delete_ShouldRemovePayment() {
        // Act
        paymentService.delete(1L);

        // Assert
        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void processPayment_ShouldSaveNewPayment() {
        // Arrange
        BigDecimal amount = new BigDecimal("150.00");
        String method = "CASH";
        String username = "testuser";

        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);

        User mockUser = new User();
        mockUser.setUsername(username);

        when(reservationService.findLastReservationByUsername(username)).thenReturn(Optional.of(mockReservation));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser)); // Мок на потребителя
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        // Act
        paymentService.processPayment(1L, amount, method, username);

        // Assert
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
