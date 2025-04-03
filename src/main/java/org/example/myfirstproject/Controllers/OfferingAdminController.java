package org.example.myfirstproject.Controllers;



import org.example.myfirstproject.Models.Entities.Offering;
import org.example.myfirstproject.Services.OfferingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin/offerings")
public class OfferingAdminController {

    private final OfferingService offeringService;

    public OfferingAdminController(OfferingService offeringService) {
        this.offeringService = offeringService;
    }

    // Показва страничката с всички оферинги
    @GetMapping
    public String showAllOfferingsForAdmin(Model model) {
        model.addAttribute("offerings", offeringService.getAllOfferingsForAdmin());
        return "offerings-admin";
    }

    // Добавяне на нова услуга
    @PostMapping("/add")
    public String addOffering(@RequestParam("name") String name,
                              @RequestParam("price") BigDecimal price,
                              @RequestParam("description") String description) {
        Offering offering = new Offering();
        offering.setName(name);
        offering.setPrice(price);
        offering.setDescription(description);
        offeringService.addOffering(offering);
        return "redirect:/admin/offerings";
    }

    // Промяна на цената на услуга
    @PostMapping("/edit/{id}")
    public String editOffering(@PathVariable Long id, @RequestParam("price") BigDecimal price) {
        offeringService.updatePrice(id, price);
        return "redirect:/admin/offerings";
    }

    // Изтриване на услуга
    @PostMapping("/delete/{id}")
    public String deleteOffering(@PathVariable Long id) {
        offeringService.deleteOffering(id);
        return "redirect:/admin/offerings";
    }
}

