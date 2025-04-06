package org.example.myfirstproject.IntegrationTests;

import org.example.myfirstproject.Models.DTO.PaymentDTO;
import org.example.myfirstproject.Models.Entities.Payment;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.PaymentRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    private User testUser;
    private Reservation testReservation;
    private Reservation anotherReservation;


    @BeforeEach
    void setUp() {
        // Изтриване на всички свързани нотификации
        notificationRepository.deleteAll();
        // Изтриване на всички записи за да сме сигурни че базата е чиста
        paymentRepository.deleteAll();
        reservationRepository.deleteAll();
        userRepository.deleteAll();

        // Създаване на тестов потребител
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
        testUser.setPhoneNumber("0888123456");
        testUser.setRole(RoleEnum.USER);
        userRepository.save(testUser);

        // Създаване на първа тестова резервация
        testReservation = new Reservation();
        testReservation.setUser(testUser);
        testReservation.setStartDate(LocalDateTime.now().plusDays(1));
        testReservation.setEndDate(LocalDateTime.now().plusDays(3));
        testReservation.setTotalPrice(BigDecimal.valueOf(100));
        reservationRepository.save(testReservation);

        // Създаване на втора тестова резервация
        anotherReservation = new Reservation();
        anotherReservation.setUser(testUser);
        anotherReservation.setStartDate(LocalDateTime.now().plusDays(4));
        anotherReservation.setEndDate(LocalDateTime.now().plusDays(6));
        anotherReservation.setTotalPrice(BigDecimal.valueOf(50));
        reservationRepository.save(anotherReservation);
    }

    @Test
    void createPayment_ShouldSaveSuccessfully() {
        // Act
        paymentService.processPayment(testReservation.getId(), BigDecimal.valueOf(50), "CASH", testUser.getUsername());

        // Assert
        List<PaymentDTO> payments = paymentService.getAllPayments();
        assertEquals(1, payments.size());
        assertEquals(PaymentMethodEnum.CASH, payments.get(0).getMethod());
        assertEquals(PaymentStatusEnum.PENDING, payments.get(0).getStatus());
    }

    @Test
    void payPayment_ShouldUpdateStatusToPaid() {
        // Arrange
        paymentService.processPayment(testReservation.getId(), BigDecimal.valueOf(50), "CASH", testUser.getUsername());

        Payment payment = paymentRepository.findAll().get(0);

        // Act
        paymentService.pay(payment.getId());

        // Assert
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        assertEquals(PaymentStatusEnum.PAID, updatedPayment.getStatus());
    }

    @Test
    void deletePayment_ShouldRemoveFromDatabase() {
        // Arrange
        paymentService.processPayment(testReservation.getId(), BigDecimal.valueOf(50), "CASH", testUser.getUsername());
        Payment payment = paymentRepository.findAll().get(0);

        // Act
        paymentService.delete(payment.getId());

        // Assert
        assertFalse(paymentRepository.findById(payment.getId()).isPresent());
    }
    @Test
    void getAllPayments_ShouldReturnCashPayment() {
        // Arrange - използваме първата резервация, създадена в setUp()
        paymentService.processPayment(testReservation.getId(), BigDecimal.valueOf(50), "CASH", testUser.getUsername());

        // Act
        List<PaymentDTO> payments = paymentService.getAllPayments();

        // Assert
        assertEquals(1, payments.size());
        assertEquals(PaymentMethodEnum.CASH, payments.get(0).getMethod());
    }


    @Test
    void getAllPayments_ShouldReturnCardPayment() {
        // Arrange - използваме втората резервация, създадена в setUp()
        paymentService.processPayment(anotherReservation.getId(), BigDecimal.valueOf(30), "CARD", testUser.getUsername());

        // Проверка дали има записано плащане в базата
        List<Payment> allPayments = paymentRepository.findAll();
        assertFalse(allPayments.isEmpty(), "Плащането не се е записало в базата!");

        // Act
        List<PaymentDTO> payments = paymentService.getAllPayments();

        // Assert
        assertEquals(1, payments.size());
        assertNotNull(payments.get(0).getMethod(), "Методът на плащане е null!");
        assertEquals(PaymentMethodEnum.CARD, payments.get(0).getMethod());
    }


}
