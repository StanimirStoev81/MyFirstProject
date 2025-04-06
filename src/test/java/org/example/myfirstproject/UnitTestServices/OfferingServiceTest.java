package org.example.myfirstproject.UnitTestServices;

import org.example.myfirstproject.Models.DTO.OfferingDTO;
import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Services.Impl.OfferingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OfferingServiceTest {

    @Mock
    private OfferingRepository offeringRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private OfferingServiceImpl offeringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void addOffering_ShouldSaveOffering() {
        // Arrange
        Set<Reservation> reservations = new HashSet<>();
        Offering offering = new Offering("Lunch", "Tasty lunch", new BigDecimal("20.00"), reservations);

        // Act
        offeringService.addOffering(offering);

        // Assert
        verify(offeringRepository, times(1)).save(offering);
    }

    @Test
    void getAllOfferings_ShouldReturnFilteredOfferings() {
        // Arrange
        Set<Reservation> reservations = new HashSet<>();
        List<Offering> offerings = new ArrayList<>();
        offerings.add(new Offering("Breakfast", "Delicious breakfast", new BigDecimal("15.00"), reservations));
        offerings.add(new Offering("Overnight Stay", "Comfortable stay", new BigDecimal("50.00"), reservations));
        offerings.add(new Offering("Spa", "Relaxing spa", new BigDecimal("30.00"), reservations));

        when(offeringRepository.findAll()).thenReturn(offerings);

        // Act
        List<OfferingDTO> result = offeringService.getAllOfferings();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(o -> o.getName().equalsIgnoreCase("Overnight Stay")));
        verify(offeringRepository, times(1)).findAll();
    }

    @Test
    void updatePrice_ShouldUpdateOfferingPrice() {
        // Arrange
        Set<Reservation> reservations = new HashSet<>();
        Offering offering = new Offering("Dinner", "Evening meal", new BigDecimal("25.00"), reservations);
        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));

        // Act
        offeringService.updatePrice(1L, new BigDecimal("30.00"));

        // Assert
        assertEquals(new BigDecimal("30.00"), offering.getPrice());
        verify(offeringRepository, times(1)).save(offering);
    }

}
