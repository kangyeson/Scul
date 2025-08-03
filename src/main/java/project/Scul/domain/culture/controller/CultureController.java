package project.Scul.domain.culture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.Scul.domain.culture.controller.dto.response.GetDetailResponse;
import project.Scul.domain.culture.service.GetCultureService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/culture")
public class CultureController {
    private final GetCultureService getCultureService;

    @GetMapping("/detail/{cultureId}")
    public GetDetailResponse getCultureDetail(@PathVariable Long userId, @PathVariable Long cultureId)
    {
        return getCultureService.getCultureDetail(userId, cultureId);
    }
}
