package project.Scul.domain.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Scul.domain.bookmark.controller.dto.response.GetUserBookmarkResponse;
import project.Scul.domain.bookmark.service.GetUserBookmarkService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/mypage/bookmark")
public class UserBookmarkController {
    private GetUserBookmarkService getUserBookmarkService;

    @GetMapping("/{userId}")
    public List<GetUserBookmarkResponse> getBookmark(@PathVariable Long userId) {
        return getUserBookmarkService.getBookmarks(userId);
    }
}
