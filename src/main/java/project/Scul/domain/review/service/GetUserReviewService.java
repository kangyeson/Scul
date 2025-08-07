package project.Scul.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Scul.domain.review.domain.Review;
import project.Scul.domain.review.controller.dto.response.GetUserReviewResponse;
import project.Scul.domain.review.repository.ReviewRepository;
import project.Scul.domain.user.service.GetUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GetUserReviewService {
    private final ReviewRepository reviewRepository;
    private final GetUserService getUserService;

    public List<GetUserReviewResponse> getUserReviews(Long userId)
    {
        String userName = getUserService.validateAndGetUsername(userId);
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
                .map(review -> new GetUserReviewResponse(userName, review)).collect(Collectors.toList());
    }
}
