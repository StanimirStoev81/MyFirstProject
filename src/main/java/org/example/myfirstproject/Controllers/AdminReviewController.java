package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.DTO.ReviewDTO;
import org.example.myfirstproject.Services.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String showReviews(Model model) {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "admin-reviews";
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/admin/reviews";
    }
}
