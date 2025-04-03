package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.Entities.Payment;
import org.example.myfirstproject.Services.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Метод за визуализация на всички плащания
    @GetMapping
    public String showPayments(Model model) {
        model.addAttribute("payments", paymentService.getAllPayments());
        return "payment";
    }

    // Метод за смяна на статуса на плащане на PAID
    @PostMapping("/pay/{id}")
    public String payPayment(@PathVariable Long id) {
        paymentService.pay(id);
        return "redirect:/admin/payments";
    }

    // Метод за изтриване на запис
    @PostMapping("/delete/{id}")
    public String deletePayment(@PathVariable Long id) {
        paymentService.delete(id);
        return "redirect:/admin/payments";
    }
}
