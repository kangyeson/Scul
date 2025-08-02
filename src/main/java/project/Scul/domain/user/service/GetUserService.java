package project.Scul.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.review.domain.Review;
import project.Scul.domain.user.controller.dto.response.GetBookmarkResponse;
import project.Scul.domain.user.controller.dto.response.GetMyPageResponse;
import project.Scul.domain.user.controller.dto.response.GetReviewResponse;
import project.Scul.domain.user.domain.User;
import project.Scul.domain.user.repository.BookmarkRepository;
import project.Scul.domain.user.repository.ReviewRepository;
import project.Scul.domain.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // 수행하는 모든 작업을 하나의 단위로 묶음 (Trnasaction : 데이터베이스의 가장 작은 수행단위)
public class GetUserService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final BookmarkRepository bookmarkRepository;

    public GetMyPageResponse getMyPage(Long userId) {
        User user = getUserOrThrow(userId);
        return new GetMyPageResponse(user);
    }

    public List<GetReviewResponse> getReviews(Long userId)
    {
        User user = getUserOrThrow(userId);
        List<Review> reviews = reviewRepository.findAllByUserId(user);
        return reviews.stream()
                .map(review -> new GetReviewResponse(user, review)).collect(Collectors.toList());
    }

    public List<GetBookmarkResponse> getBookmarks(Long userId)
    {
        User user = getUserOrThrow(userId);
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUser(user);
        return bookmarks.stream()
                .map(bookmark -> new GetBookmarkResponse(bookmark.getCultureId())).collect(Collectors.toList());
    }

    private User getUserOrThrow(Long userId)
    {
        return userRepository.findUserByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }
}
