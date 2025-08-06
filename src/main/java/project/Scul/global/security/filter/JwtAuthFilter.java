package project.Scul.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import project.Scul.domain.user.service.GetUserService;
import project.Scul.global.security.config.JwtProperties;
import project.Scul.global.security.jwt.JwtProvider;

import java.io.IOException;
import java.util.Collections;

// 요청을 필터링하고 JWT를 사용하여 인증 및 권한 부여를 처리하는 필터
@Component
@RequiredArgsConstructor
// 요청 당 한번의 filter를 수행하기 위해 OncePerRequestFilter를 상속받음
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final GetUserService getUserService;

    /**
     * HTTP 요청이 들어올 때마다 호출되는 메서드
     * - JWT 토큰 유효성 검증 및 사용자 인증 객체 등록을 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = request.getHeader("Authorization"); // 요청 헤더에서 "Authorization" 헤더의 값을 가져옴

        String userId = null; // 사용자 ID를 저장할 변수 (JWT에서 추출)

        // Authorization 헤더(토큰)가 존재하고 비어있지 않은 경우
        if (token != null && !token.isEmpty()){
            String jwtToken = token.substring(7); // "Bearer " 접두어 제거 (총 7글자)

            userId = jwtProvider.getUserIdFromToken(jwtToken); // JWT 토큰에서 userId 추출
        }

        // token 검증 완료 후, 사용자가 존재하는데 아직 Spring SecurityContext에 인증 정보(USER/ADMIN)가 등록되지 않았다면
        if(userId != null && !userId.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){
            // 사용자 인증 객체 생성 및 SecurityContext에 등록
            SecurityContextHolder.getContext().setAuthentication(getUserAuth(userId));
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달 (필터 체인 계속 진행)
    }

    /**
     * 사용자 인증 객체를 생성하는 메서드
     * - token의 사용자 idx를 이용하여 DB에서 사용자 정보를 조회하고, 인증 토큰(UsernamePasswordAuthenticationToken을 만들어 반환
     *
     * `UsernamePasswordAuthenticationToken`객체를 직접 생성해서 `userId`, `password`, `autority`정보를 담고
     *  이 객체를 `SecurityContextHolder`에 `Authentication`(사용자 인증 객체)로 설정하면 인증된 사용자로 간주됨!
     *
     * @param userId 사용자 식별자 (JWT에서 추출됨)
     * @return UsernamePasswordAuthenticationToken 인증 객체
     */
    private UsernamePasswordAuthenticationToken getUserAuth(String userId){
        var userInfo = getUserService.getUserOrThrow(Long.parseLong(userId)); // 사용자 ID(Long)로 사용자 정보 조회

        // 사용자 인증 토큰 생성 (principal = userId, credential = password, 권한 = ROLE_USER)
        return new UsernamePasswordAuthenticationToken(userInfo.getUserId(),
                userInfo.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
                // SimpleGrantedAuthority("ROLE_USER") : User단계의 "ROLE_USER"라는 하나의 권한 객체를 생성
                // Collections.singleton(...) : 하나의 권한만 담긴 Set 컬렉션 생성
                // + 접근 제어 사용자 권한은 hasRole("USER")과 같은 방식으로 검사 가능
    }
}
