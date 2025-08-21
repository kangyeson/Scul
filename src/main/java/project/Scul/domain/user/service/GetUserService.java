package project.Scul.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import project.Scul.domain.user.controller.dto.response.GetMyPageResponse;
import project.Scul.domain.user.domain.User;
import project.Scul.domain.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // 수행하는 모든 작업을 하나의 단위로 묶음 (Trnasaction : 데이터베이스의 가장 작은 수행단위)
public class GetUserService {
    private final UserRepository userRepository;

    public GetMyPageResponse getMyPage(Long userId) {
        User user = getUserOrThrow(userId);
        return new GetMyPageResponse(user);
    }

    public String validateAndGetUsername(Long userId) {
        User user = getUserOrThrow(userId);
        return user.getName();
    }

    public User getUserOrThrow(Long userId)
    {
        return userRepository.findUserByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    // UserDetails : Spring Security에서 사용자의 인증 정보를 담는 핵심 객체(인터페이스)
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        // accountId로 사용자 조회
        User user = userRepository.findUserByAccountId(accountId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // UserDetails 객체로 변환하여 반환
        return new org.springframework.security.core.userdetails.User(
                user.getAccountId(), // 사용자 식별자
                user.getPassword(), // 비밀번호
                Collections.emptyList() // 권한 목록 (authorities) : 프로젝트 특성상 권한을 나눌 필요가 없음(빈 리스트)
                // 권한이 필요한 프로젝트였다면 : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
