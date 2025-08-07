package project.Scul.domain.review.controller;

import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Scul.domain.review.service.GetUserReviewService;
import project.Scul.domain.review.controller.dto.response.GetUserReviewResponse;

import java.util.List;

@RestController
@NoArgsConstructor
@RequestMapping("/user/mypage/review")
public class UserReviewController {
    private GetUserReviewService getUserReviewService;

    @GetMapping("/{userId}")
    public List<GetUserReviewResponse> getReview(@PathVariable Long userId) {
        return getUserReviewService.getUserReviews(userId);
    }
}
