package project.Scul.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.user.domain.User;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findAllByUser(User user);

    boolean existsByUserIdAndCultureId(Long userId, Long cultureId);
}
