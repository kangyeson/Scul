package project.Scul.domain.review.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.user.domain.User;

import java.time.LocalDate;

@Getter
@Entity(name = "tbl_review")
@NoArgsConstructor(force = true)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_id")
    private Culture cultureId;

    private LocalDate reviewedDate;

    @Column(columnDefinition = "VARCHAR(60)")
    private String imageUrl;

    @Column(columnDefinition = "VARCHAR(60)")
    private String reviewContent;

    @Builder
    public Review(Long reviewId, User userId, Culture cultureId, String imageUrl, String reviewContent, LocalDate reviewedDate) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.cultureId = cultureId;
        this.imageUrl = imageUrl;
        this.reviewContent = reviewContent;
        this.reviewedDate = reviewedDate;
    }
}
