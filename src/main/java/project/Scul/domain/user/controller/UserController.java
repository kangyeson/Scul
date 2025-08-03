package project.Scul.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.Scul.domain.bookmark.domain.Bookmark;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.review.domain.Review;
import project.Scul.domain.user.controller.dto.response.GetBookmarkResponse;
import project.Scul.domain.user.controller.dto.response.GetMyPageResponse;
import project.Scul.domain.user.controller.dto.response.GetReviewResponse;
import project.Scul.domain.user.domain.User;
import project.Scul.domain.user.repository.*;
import project.Scul.domain.user.repository.ReviewRepository;
import project.Scul.domain.user.repository.UserRepository;
import project.Scul.domain.user.service.GetUserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController // REST API를 만들 때 사용하며, 응답 값을 자동으로 JSON 등으로 직렬화해
@RequiredArgsConstructor // 생성자 자동 생성
@RequestMapping("/user") // 페이지 컨트롤러 공통 url 지정 ex) /user/my-page
public class UserController {
    private final GetUserService getUserService;

    @GetMapping("/mypage/{userId}")
    public GetMyPageResponse getMyPage(@PathVariable Long userId) {
        return getUserService.getMyPage(userId);
    }

    @GetMapping("/mypage/review/{userId}")
    public List<GetReviewResponse> getReview(@PathVariable Long userId) {
       return getUserService.getReviews(userId);
    }

    @GetMapping("/mypage/bookmark/{userId}")
    public List<GetBookmarkResponse> getBookmark(@PathVariable Long userId) {
       return getUserService.getBookmarks(userId);
    }
}
