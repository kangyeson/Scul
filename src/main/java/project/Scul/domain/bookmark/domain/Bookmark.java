package project.Scul.domain.bookmark.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.user.domain.User;

@Getter
@Entity(name = "tbl_bookmark")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "culture_id")
    private Culture cultureId;

    @Builder
    public Bookmark(Long bookmarkId, User userId, Culture cultureId) {
        this.bookmarkId = bookmarkId;
        this.userId = userId;
        this.cultureId = cultureId;
    }
}
