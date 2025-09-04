package project.Scul.domain.culture.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.Scul.domain.culture.controller.dto.response.GetDetailResponse;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.culture.repository.CultureRepository;
import project.Scul.domain.bookmark.repository.BookmarkRepository;
import project.Scul.domain.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional
public class GetCultureService {
    private final CultureRepository cultureRepository;
    private final BookmarkRepository bookmarkRepository;

    public GetDetailResponse getCultureDetail(Long userId, Long cultureId) {
        Culture culture = cultureRepository.findById(cultureId).orElse(null);
        boolean IsBookmarked = bookmarkRepository.existsByUser_UserIdAndCulture_CultureId(userId, cultureId);
        return new GetDetailResponse(culture, IsBookmarked);
    }
}
