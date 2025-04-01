package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.DTO.EndReservationDTO;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Services.KafkaProducerService;
import org.example.myfirstproject.Services.PaymentService;
import org.example.myfirstproject.Services.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
@RequestMapping("/user/reservation")
public class EndReservationController {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final KafkaProducerService kafkaProducerService;
    public EndReservationController(ReservationService reservationService, PaymentService paymentService, KafkaProducerService kafkaProducerService) {
        this.reservationService = reservationService;
        this.paymentService = paymentService;

        this.kafkaProducerService = kafkaProducerService;
    }

    @GetMapping("/endReservation")
    public String showEndReservation(Model model, Authentication authentication) {
        String username = authentication.getName();

        Optional<Reservation> optionalReservation = reservationService.findLastReservationByUsername(username);

        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            EndReservationDTO dto = new EndReservationDTO(
                    reservation.getId(),
                    reservation.getTotalPrice(),
                    reservation.getTotalOfferingPrice()
            );
            model.addAttribute("reservation", dto);
            BigDecimal totalAmount = reservation.getTotalPrice().add(reservation.getTotalOfferingPrice());
            String notificationMessage = "Your reservation is completed! Total: " + totalAmount + " BGN";
            Long userId;
            if (username.equals("Stambeto_81")) {  // Ако администраторът е логнат
                userId = 1L; // Взимаме ID-то на администратора (примерно)
            } else {
                userId = reservation.getUser().getId(); // Взимаме ID-то на потребителя
            }// Взимаме ID-то на потребителя
            kafkaProducerService.sendNotification(userId, notificationMessage);
        } else {
            model.addAttribute("error", "No reservation found!");
            model.addAttribute("reservation", new EndReservationDTO()); // Изпращаме празен обект, за да няма null
        }

        return "endReservation";
    }
    @PostMapping("/pay")
    public String processPayment(
            @RequestParam("reservationId") Long reservationId,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("method") String method,
            Authentication authentication) {

        String username = authentication.getName();

        // Обработваме плащането
        paymentService.processPayment(reservationId, amount, method, username);

        // Пренасочваме към home страницата след успешното плащане
        if(username.equals("Stambeto_81")){
            return "redirect:/adminHome";
        }
        return "redirect:/userHome";
    }

}
