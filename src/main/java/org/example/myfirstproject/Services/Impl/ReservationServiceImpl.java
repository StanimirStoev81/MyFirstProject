package org.example.myfirstproject.Services.Impl;


import org.example.myfirstproject.Models.DTO.ReservationDTO;
import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.ReservationStatusEnum;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.PaymentRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.ReservationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public  class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final OfferingRepository offeringRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, OfferingRepository offeringRepository, UserRepository userRepository, PaymentRepository paymentRepository) {
        this.reservationRepository = reservationRepository;
        this.offeringRepository = offeringRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }



    @Override
    @Transactional
    public void createReservation(ReservationDTO reservationDTO, List<Long> selectedOfferingIds) {
        // Намираме потребителя по email
        Optional<User> userOpt = userRepository.findByEmail(reservationDTO.getEmail());
        User user;
        if (userOpt.isEmpty()) {
            // 🟢 Ако потребителят не бъде намерен, вземаме админа с ID = 1
            user = userRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("Admin not found!"));
        } else {
            // 🟢 Ако намерим потребител, взимаме него
            user = userOpt.get();
        }

        // 🟢 Изчисляваме броя нощувки
        long nights = ChronoUnit.DAYS.between(reservationDTO.getStartDate(), reservationDTO.getEndDate());
        if (nights < 1) {
            throw new IllegalArgumentException("End date must be after start date!");
        }

        // 🔵 Взимаме цената за нощувка (Overnight Stay)
        Offering overnightStay = offeringRepository.findByName("Overnight Stay")
                .orElseThrow(() -> new IllegalArgumentException("Overnight Stay offering not found!"));
        BigDecimal totalPrice = overnightStay.getPrice().multiply(BigDecimal.valueOf(nights));

        // 🟡 Изчисляваме totalOfferingPrice (избрани услуги)
        Set<Offering> selectedOfferings = selectedOfferingIds.stream()
                .map(offeringRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        BigDecimal totalOfferingPrice = selectedOfferings.stream()
                .map(Offering::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 🔴 Запазваме резервацията
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDate(reservationDTO.getStartDate());
        reservation.setEndDate(reservationDTO.getEndDate());
        reservation.setStatus(ReservationStatusEnum.PENDING);
        reservation.setTotalPrice(totalPrice);
        reservation.setTotalOfferingPrice(totalOfferingPrice);
        reservation.setOfferings(selectedOfferings);

        reservationRepository.save(reservation);
    }

    public Optional<Reservation> findLastReservationByUsername(String username) {
        return userRepository.findByUsername(username)
                .flatMap(user -> reservationRepository.findTopByUserOrderByIdDesc(user));
    }
    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteReservationById(Long id) {
        // Първо изтриваме всички свързани плащания
        paymentRepository.deleteByReservationId(id);

        // След това изтриваме резервацията
        reservationRepository.deleteById(id);
    }
}
