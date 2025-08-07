package project.Scul.domain.review.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.review.domain.Review;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class GetUserReviewResponse {
    private String userName;
    private Long reviewId;
    private String place;
    private LocalDate reviewedDate;
    private String reviewContent;

    public GetUserReviewResponse(String userName, Review review) {
        reviewId = review.getReviewId();
        place = review.getCultureId().getPlace(); // 연관된 장소명
        reviewedDate = review.getReviewedDate();
        reviewContent = review.getReviewContent();
    }
}
