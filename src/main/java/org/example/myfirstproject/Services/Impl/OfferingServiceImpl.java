package org.example.myfirstproject.Services.Impl;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.DTO.OfferingDTO;

import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Models.Entities.Reservation;
import org.example.myfirstproject.Repositories.OfferingRepository;
import org.example.myfirstproject.Repositories.ReservationRepository;
import org.example.myfirstproject.Services.OfferingService;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferingServiceImpl implements OfferingService {

    private final OfferingRepository offeringRepository;
    private final ReservationRepository reservationRepository;

    public OfferingServiceImpl(OfferingRepository offeringRepository, ReservationRepository reservationRepository) {
        this.offeringRepository = offeringRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public List<OfferingDTO> getAllOfferings() {
        List<OfferingDTO> offeringList = offeringRepository.findAll().stream()
                .filter(offering -> !offering.getName().equalsIgnoreCase("Overnight Stay"))
                .map(offering -> new OfferingDTO(offering.getId(), offering.getName(), offering.getPrice(),offering.getDescription()))
                .collect(Collectors.toList());

        System.out.println("Offerings loaded from DB: " + offeringList); // ✅ Лог на офертите

        return offeringList;
    }
    @Override
    public void addOffering(Offering offering) {
        offeringRepository.save(offering);
    }

    @Override
    public void updatePrice(Long id, BigDecimal price) {
        Offering offering = offeringRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Offering not found"));
        offering.setPrice(price);
        offeringRepository.save(offering);
    }

    @Override
    public void deleteOffering(Long id) {
        offeringRepository.deleteById(id);
    }
    @Override
    public List<OfferingDTO> getAllOfferingsForAdmin() {
        List<OfferingDTO> offeringList = offeringRepository.findAll().stream()
                .map(offering -> new OfferingDTO(offering.getId(), offering.getName(), offering.getPrice(),offering.getDescription()))
                .collect(Collectors.toList());

        System.out.println("Admin Offerings loaded from DB: " + offeringList); // ✅ Лог на офертите

        return offeringList;
    }

}
