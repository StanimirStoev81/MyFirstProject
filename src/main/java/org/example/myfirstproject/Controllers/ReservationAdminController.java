package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Services.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/reservations")
public class ReservationAdminController {

    private final ReservationService reservationService;

    public ReservationAdminController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getAllReservations(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);
        return "reservation-admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return "redirect:/admin/reservations";
    }
}

