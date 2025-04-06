package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.DTO.OfferingDTO;
import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Services.OfferingService;
import org.example.myfirstproject.Services.ReservationService;
import org.example.myfirstproject.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ReservationService reservationService;

    @MockitoSpyBean
    private OfferingService offeringService;

    @MockitoSpyBean
    private UserService userService;

    // 🟢 API тест за GET заявка към /user/reservation
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void showReservationForm_ShouldReturnReservationUserPage() throws Exception {
        // Мокваме връщането на списък с оферти
        when(offeringService.getAllOfferings())
                .thenReturn(List.of(new OfferingDTO(1L, "Breakfast", BigDecimal.valueOf(10.00), "Delicious breakfast")));


        mockMvc.perform(get("/user/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservationUser"))
                .andExpect(model().attributeExists("offerings"));
    }



    // 🔵 API тест за POST заявка към /user/reservation
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void postReservation_ShouldReturnRedirect() throws Exception {
        // Подготвяме DTO за резервация
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setFirstName("John");
        reservationDTO.setLastName("Doe");
        reservationDTO.setEmail("john.doe@example.com");

        doNothing().when(reservationService).createReservation(Mockito.any(ReservationDTO.class), Mockito.anyList());

        mockMvc.perform(post("/user/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("selectedOfferingIds", "1", "2")
                        .flashAttr("reservationDTO", reservationDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/reservation/endReservation"));

        verify(reservationService, times(1)).createReservation(Mockito.any(ReservationDTO.class), Mockito.anyList());
    }
}
