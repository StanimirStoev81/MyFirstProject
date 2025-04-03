package org.example.reviewservice.Service;

import org.example.reviewservice.Models.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    List<Review> getAllReviews();
    Review getReviewById(Long id);
    void deleteReview(Long id);
}
