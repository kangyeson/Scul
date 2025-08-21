package project.Scul.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import project.Scul.domain.user.service.GetUserService;
import project.Scul.global.security.config.JwtProperties;
import project.Scul.global.security.jwt.JwtProvider;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT 토큰을 사용하여 사용자를 인증하고,
 * 스프링 시큐리티 컨텍스트에 인증 정보를 등록하는 역할 -> '로그인된 사용자'로 인식시켜줌
 */

// 요청을 필터링하고 JWT를 사용하여 인증 및 권한 부여를 처리하는 필터
@Component
@RequiredArgsConstructor
// 요청 당 한번의 filter를 수행하기 위해 OncePerRequestFilter를 상속받음
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final GetUserService getUserService; // DB에서 사용자 정보를 가져오는 서비스

    /**
     * HTTP 요청이 들어올 때마다 호출되는 메서드
     * - JWT 토큰 유효성 검증 및 사용자 인증 객체 등록을 처리
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = resolveToken(request); // JWT 토큰 추출

        String userId = null; // 사용자 ID를 저장할 변수 (JWT에서 추출)

        // 토큰이 존재하고 유효한 경우
        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            // 토큰에서 accountId 추출
            String accountId = jwtProvider.getAccountIdFromToken(accessToken);

            // SecurityContext에 아직 인증 정보(Authentication)가 없는 경우
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // DB에서 사용자 정보 조회 (아이디, 비밀번호 등을 담은 UserDetails객체 반환)
                UserDetails userDetails = getUserService.loadUserByUsername(accountId);

                // 사용자가 존재할 경우, 인증 객체 생성 및 등록
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    
                    // 앞서 생성한 인증 객체를 SecurityContextHolder에 저장 -> 현재 요청을 '인증된 상태'로 간주하게 됨
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 요청 전달 (필터 체인 계속 진행)
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
