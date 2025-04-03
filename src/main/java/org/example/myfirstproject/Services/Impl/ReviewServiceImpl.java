package org.example.myfirstproject.Services.Impl;

import org.example.myfirstproject.Models.DTO.ReviewDTO;
import org.example.myfirstproject.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    @Qualifier("reviewServiceClient")
    private final WebClient reviewServiceClient;

    public ReviewServiceImpl(WebClient reviewServiceClient) {
        this.reviewServiceClient = reviewServiceClient;
    }

    @Override
    public List<ReviewDTO> getAllReviews() {
        return Arrays.asList(reviewServiceClient.get()
                .uri("")
                .retrieve()
                .bodyToMono(ReviewDTO[].class)
                .block());
    }

    @Override
    public ReviewDTO getReviewById(Long id) {
        return reviewServiceClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(ReviewDTO.class)
                .block();
    }

    @Override
    public ReviewDTO createReview(ReviewDTO review) {
        return reviewServiceClient.post()
                .uri("")
                .bodyValue(review)
                .retrieve()
                .bodyToMono(ReviewDTO.class)
                .block();
    }

    @Override
    public void deleteReview(Long id) {
        reviewServiceClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
