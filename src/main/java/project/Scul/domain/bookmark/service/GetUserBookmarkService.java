package project.Scul.domain.bookmark.service;

import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import project.Scul.domain.bookmark.controller.dto.response.GetUserBookmarkResponse;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.bookmark.repository.BookmarkRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@Transactional
public class GetUserBookmarkService {
    private BookmarkRepository bookmarkRepository;

    public List<GetUserBookmarkResponse> getBookmarks(Long userId)
    {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserId(userId);
        return bookmarks.stream()
                .map(bookmark -> new GetUserBookmarkResponse(bookmark.getCultureId())).collect(Collectors.toList());
    }
}
