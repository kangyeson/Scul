package project.Scul.domain.user.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.review.domain.Review;
import project.Scul.domain.user.domain.User;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetReviewResponse {
    private String userName;
    private Long reviewId;
    private String place;
    private LocalDate reviewedDate;
    private String reviewContent;

    public GetReviewResponse(User user, Review review) {
        userName = user.getName();
        reviewId = review.getReviewId();
        place = review.getCultureId().getPlace(); // 연관된 장소명
        reviewedDate = review.getReviewedDate();
        reviewContent = review.getReviewContent();
    }
}
