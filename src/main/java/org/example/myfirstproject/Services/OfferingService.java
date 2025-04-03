package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.OfferingDTO;
import org.example.myfirstproject.Models.Entities.Offering;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface OfferingService {
    List<OfferingDTO> getAllOfferings();
    void addOffering(Offering offering);
    void updatePrice(Long id, BigDecimal price);
    void deleteOffering(Long id);

}
