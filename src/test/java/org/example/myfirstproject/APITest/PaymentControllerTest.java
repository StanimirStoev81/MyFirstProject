package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.DTO.PaymentDTO;
import org.example.myfirstproject.Models.Enums.PaymentMethodEnum;
import org.example.myfirstproject.Models.Enums.PaymentStatusEnum;
import org.example.myfirstproject.Services.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private PaymentService paymentService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void showPayments_ShouldReturnPaymentPage() throws Exception {
        // Arrange
        PaymentDTO paymentDTO = new PaymentDTO(1L, "John", "Doe", BigDecimal.valueOf(100.00), PaymentMethodEnum.CASH, PaymentStatusEnum.PENDING);
        when(paymentService.getAllPayments()).thenReturn(Collections.singletonList(paymentDTO));

        // Act & Assert
        mockMvc.perform(get("/admin/payments"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment"))
                .andExpect(model().attributeExists("payments"))
                .andExpect(model().attribute("payments", List.of(paymentDTO)));

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void payPayment_ShouldRedirectToPaymentsPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/admin/payments/pay/{id}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/payments"));

        verify(paymentService, times(1)).pay(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deletePayment_ShouldRedirectToPaymentsPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/admin/payments/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/payments"));

        verify(paymentService, times(1)).delete(1L);
    }
}
