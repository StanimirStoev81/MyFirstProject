package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.DTO.EndReservationDTO;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Services.KafkaProducerService;
import org.example.myfirstproject.Services.PaymentService;
import org.example.myfirstproject.Services.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EndReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ReservationService reservationService;

    @MockitoSpyBean
    private PaymentService paymentService;

    @MockitoSpyBean
    private KafkaProducerService kafkaProducerService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void showEndReservation_ShouldReturnEndReservationPage() throws Exception {
        // Arrange
        User testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setTotalPrice(BigDecimal.valueOf(100.00));
        reservation.setTotalOfferingPrice(BigDecimal.valueOf(50.00));
        reservation.setUser(testUser);

        when(reservationService.findLastReservationByUsername("testuser")).thenReturn(Optional.of(reservation));

        // Act & Assert
        mockMvc.perform(get("/user/reservation/endReservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("endReservation"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attribute("reservation",
                        org.hamcrest.Matchers.hasProperty("id", org.hamcrest.Matchers.equalTo(1L))))
                .andExpect(model().attribute("reservation",
                        org.hamcrest.Matchers.hasProperty("totalPrice", org.hamcrest.Matchers.comparesEqualTo(BigDecimal.valueOf(100.00)))))
                .andExpect(model().attribute("reservation",
                        org.hamcrest.Matchers.hasProperty("totalOfferingPrice", org.hamcrest.Matchers.comparesEqualTo(BigDecimal.valueOf(50.00)))));

        // Verify Kafka notification
        verify(kafkaProducerService, times(1)).sendNotification(eq(1L), contains("Your reservation is completed! Total: 150.00 BGN"));
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void processPayment_ShouldRedirectToUserHome() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/user/reservation/pay")
                        .param("reservationId", "1")
                        .param("amount", "150.00")
                        .param("method", "CASH"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userHome"));

        // Verify payment processing
        verify(paymentService, times(1)).processPayment(1L, new BigDecimal("150.00"), "CASH", "testuser");
    }

    @Test
    @WithMockUser(username = "Stambeto_81", roles = {"ADMIN"})
    void processPayment_AsAdmin_ShouldRedirectToAdminHome() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/user/reservation/pay")
                        .param("reservationId", "1")
                        .param("amount", "150.00")
                        .param("method", "CARD"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adminHome"));

        // Verify payment processing
        verify(paymentService, times(1)).processPayment(1L, new BigDecimal("150.00"), "CARD", "Stambeto_81");
    }

}
