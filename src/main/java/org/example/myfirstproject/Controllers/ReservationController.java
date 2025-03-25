package org.example.myfirstproject.Controllers;


import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Services.OfferingService;
import org.example.myfirstproject.Services.ReservationService;
import org.example.myfirstproject.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final OfferingService offeringService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, OfferingService offeringService, UserService userService) {
        this.reservationService = reservationService;
        this.offeringService = offeringService;
        this.userService = userService;
    }

    // 🟢 GET заявка - Показва reservationUser.html
    @GetMapping
    public String showReservationForm(Model model) {
        model.addAttribute("offerings", offeringService.getAllOfferings());
        return "reservationUser";
    }

    // 🔵 POST заявка - Записва резервацията
    @PostMapping
    public String processReservation(@ModelAttribute ReservationDTO reservationDTO,
                                     @RequestParam("selectedOfferingIds") List<Long> selectedOfferingIds) {

        reservationService.createReservation(reservationDTO, selectedOfferingIds);

        return "redirect:/user/reservation/endReservation";
    }
}
