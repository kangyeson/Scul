package project.Scul.domain.bookmark.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.user.domain.User;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    // User 엔티티의 기본키가 userId일 경우
    List<Bookmark> findAllByUser_UserId(Long userId);

    boolean existsByUser_UserIdAndCulture_CultureId(Long userId, Long cultureId);
}
