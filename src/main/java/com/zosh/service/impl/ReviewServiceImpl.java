package com.zosh.service.impl;

import com.zosh.modal.Product;
import com.zosh.modal.Review;
import com.zosh.modal.User;
import com.zosh.repository.ReviewRepository;
import com.zosh.request.CreateReviewRequest;
import com.zosh.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
    Review review = getReviewById(reviewId);
    if(Objects.equals(review.getUser().getId(), userId)){
        review.setReviewText(reviewText);
        review.setRating(rating);
        return reviewRepository.save(review);
    }
         throw new Exception("you cant update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
        Review review = getReviewById(reviewId);
        if(Objects.equals(review.getUser().getId(), userId)){
            throw new Exception("you cant delete this review");
        }
        reviewRepository.delete(review);
    }


    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(
                ()->new Exception("review not found"));
    }
}
