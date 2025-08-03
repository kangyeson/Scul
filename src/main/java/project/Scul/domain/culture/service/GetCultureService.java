package project.Scul.domain.culture.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.Scul.domain.culture.controller.dto.response.GetDetailResponse;
import project.Scul.domain.culture.domain.Culture;
import project.Scul.domain.culture.repository.CultureRepository;
import project.Scul.domain.user.domain.User;
import project.Scul.domain.user.repository.BookmarkRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class GetCultureService {
    private final CultureRepository cultureRepository;
    private final BookmarkRepository bookmarkRepository;

    public GetDetailResponse getCultureDetail(Long cultureId, Long userId) {
        Culture culture = cultureRepository.findById(cultureId).orElse(null);
        boolean IsBookmarked = bookmarkRepository.existsByUserIdAndCultureId(userId, cultureId);
        return new GetDetailResponse(culture, IsBookmarked);
    }
}
