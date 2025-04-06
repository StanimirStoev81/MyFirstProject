package org.example.myfirstproject.IntegrationTests;

import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.ReservationStatusEnum;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Създаване на потребител
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
        testUser.setPhoneNumber("123456789"); // Добавяме phone number
        testUser.setRole(RoleEnum.USER); // Добавяме роля на потребителя
        userRepository.save(testUser);

        // Създаване на услуга
        Offering offering = new Offering("Breakfast", "Delicious breakfast", BigDecimal.valueOf(10), Set.of());
        offeringRepository.save(offering);
    }


    @Test
    void createReservation_ShouldSaveSuccessfully() {
        // Проверка на броя резервации преди създаване
        int initialReservationCount = reservationRepository.findAll().size();
        System.out.println("Initial reservations count: " + initialReservationCount);

        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO(
                testUser.getFirstName(),
                testUser.getLastName(),
                testUser.getEmail(),
                testUser.getPhoneNumber(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(50),
                List.of()
        );

        List<Long> offeringIds = offeringRepository.findAll().stream()
                .map(Offering::getId)
                .filter(id -> id > 6)  // 🟢 Използваме само новосъздадените оферинги
                .toList();

        // Act
        reservationService.createReservation(reservationDTO, offeringIds);

        // Assert
        List<Reservation> reservations = reservationRepository.findAll();
        System.out.println("Reservations count after creation: " + reservations.size());

        assertEquals(initialReservationCount + 1, reservations.size());
        assertEquals(ReservationStatusEnum.PENDING, reservations.get(initialReservationCount).getStatus());
    }

    @Test
    void createReservation_ShouldLinkToUser() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO(
                "Test",
                "User",
                testUser.getEmail(),
                "123456789",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(10),
                List.of()
        );

        // Act
        reservationService.createReservation(reservationDTO, List.of());

        // Assert
        Reservation reservation = reservationRepository.findAll().get(0);
        assertEquals(testUser.getUsername(), reservation.getUser().getUsername());
    }


    @Test
    void deleteReservation_ShouldRemoveFromDatabase() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setUser(testUser);
        reservation.setStartDate(LocalDateTime.now().plusDays(1));
        reservation.setEndDate(LocalDateTime.now().plusDays(3));
        reservation.setTotalPrice(BigDecimal.valueOf(100));
        reservationRepository.save(reservation);

        // Act
        reservationService.deleteReservationById(reservation.getId());

        // Assert
        assertFalse(reservationRepository.findById(reservation.getId()).isPresent());
    }

    @Test
    void getAllReservations_ShouldReturnAll() {
        // Arrange
        Reservation reservation1 = new Reservation();
        reservation1.setUser(testUser);
        reservation1.setStartDate(LocalDateTime.now().plusDays(1));
        reservation1.setEndDate(LocalDateTime.now().plusDays(3));
        reservation1.setTotalPrice(BigDecimal.valueOf(100));

        Reservation reservation2 = new Reservation();
        reservation2.setUser(testUser);
        reservation2.setStartDate(LocalDateTime.now().plusDays(4));
        reservation2.setEndDate(LocalDateTime.now().plusDays(6));
        reservation2.setTotalPrice(BigDecimal.valueOf(200));

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> reservations = reservationService.getAllReservations();

        // Assert
        assertEquals(2, reservations.size());
    }
}
