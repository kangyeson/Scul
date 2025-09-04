package project.Scul.domain.bookmark.service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import project.Scul.domain.bookmark.controller.dto.response.GetUserBookmarkResponse;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.bookmark.repository.BookmarkRepository;
import project.Scul.domain.culture.domain.Culture;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Transactional
public class GetUserBookmarkService {
    private BookmarkRepository bookmarkRepository;

    public GetUserBookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<GetUserBookmarkResponse> getBookmarks(Long userId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser_UserId(userId); // 수정된 메서드 이름 사용
        return bookmarks.stream()
                .map(bookmark -> {
                    Culture culture = bookmark.getCulture();
                    return new GetUserBookmarkResponse(
                            culture.getPlace(),
                            culture.getAddress(),
                            culture.isReservable(),
                            culture.getUsageFee(),
                            culture.getTag(),
                            culture.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }
}
