package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getAllReviews();
    ReviewDTO getReviewById(Long id);
    ReviewDTO createReview(ReviewDTO review);
    void deleteReview(Long id);
}
