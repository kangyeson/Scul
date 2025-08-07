package project.Scul.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.Scul.domain.user.controller.dto.response.GetMyPageResponse;
import project.Scul.domain.user.service.GetUserService;

@RestController // REST API를 만들 때 사용하며, 응답 값을 자동으로 JSON 등으로 직렬화해
@RequiredArgsConstructor // 생성자 자동 생성
@RequestMapping("/user") // 페이지 컨트롤러 공통 url 지정 ex) /user/my-page
public class UserController {
    private final GetUserService getUserService;

    @GetMapping("/mypage/{userId}")
    public GetMyPageResponse getMyPage(@PathVariable Long userId) {
        return getUserService.getMyPage(userId);
    }
}
