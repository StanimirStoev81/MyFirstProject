package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationService {



        void createReservation(ReservationDTO reservationDTO, List<Long> selectedOfferingIds);


    Optional<Reservation> findLastReservationByUsername(String username);
}

