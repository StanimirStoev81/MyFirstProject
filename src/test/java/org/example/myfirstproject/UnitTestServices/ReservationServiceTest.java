package org.example.myfirstproject.UnitTestServices;

import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.PaymentRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.Impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createReservation_ShouldSaveReservation() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmail("test@example.com");
        reservationDTO.setStartDate(LocalDateTime.now());
        reservationDTO.setEndDate(LocalDateTime.now().plusDays(2));
        List<Long> offeringIds = List.of(1L, 2L);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Offering overnightStay = new Offering("Overnight Stay", "Description", new BigDecimal("50.00"), new HashSet<>());
        Offering additionalOffering = new Offering("Breakfast", "Tasty breakfast", new BigDecimal("20.00"), new HashSet<>());

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(offeringRepository.findByName("Overnight Stay")).thenReturn(Optional.of(overnightStay));
        when(offeringRepository.findById(1L)).thenReturn(Optional.of(additionalOffering));
        when(offeringRepository.findById(2L)).thenReturn(Optional.empty());

        // Act
        reservationService.createReservation(reservationDTO, offeringIds);

        // Assert
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void findLastReservationByUsername_ShouldReturnReservation() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(reservationRepository.findTopByUserOrderByIdDesc(user)).thenReturn(Optional.of(reservation));

        // Act
        Optional<Reservation> result = reservationService.findLastReservationByUsername("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getAllReservations_ShouldReturnAllReservations() {
        // Arrange
        List<Reservation> reservations = Arrays.asList(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getAllReservations();

        // Assert
        assertEquals(2, result.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void deleteReservationById_ShouldDeleteReservationAndPayments() {
        // Act
        reservationService.deleteReservationById(1L);

        // Assert
        verify(paymentRepository, times(1)).deleteByReservationId(1L);
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void createReservation_ShouldThrowException_WhenEndDateIsBeforeStartDate() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmail("test@example.com");
        reservationDTO.setStartDate(LocalDateTime.now());
        reservationDTO.setEndDate(LocalDateTime.now().minusDays(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO, new ArrayList<>()));
    }

    @Test
    void createReservation_ShouldThrowException_WhenOvernightStayNotFound() {
        // Arrange
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setEmail("test@example.com");
        reservationDTO.setStartDate(LocalDateTime.now());
        reservationDTO.setEndDate(LocalDateTime.now().plusDays(2));

        when(offeringRepository.findByName("Overnight Stay")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservationDTO, new ArrayList<>()));
    }
}
