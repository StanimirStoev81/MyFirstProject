package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.DTO.ReviewDTO;
import org.example.myfirstproject.Services.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/user/review")
public class UserReviewController {

    private final ReviewService reviewService;

    public UserReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public String showReviewForm() {
        return "user-review";
    }
    @PostMapping("/submit")
    public String submitReview(@RequestParam("rating") int rating,
                               @RequestParam("message") String message,
                               Authentication authentication) {
        String username = authentication.getName();
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setUsername(username);
        reviewDTO.setMessage(message);
        reviewDTO.setRating(rating);
        reviewDTO.setTimestamp(LocalDateTime.now());

        reviewService.createReview(reviewDTO);
        return "redirect:/userHome";
    }
}
