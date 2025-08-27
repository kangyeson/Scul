package project.Scul.global.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import project.Scul.global.security.filter.JwtAuthFilter;
import project.Scul.global.security.handler.CustomAccessDeniedHandler;
import project.Scul.global.security.handler.CustomAuthenticationEntryPointHandler;

/**
 * Spring Security 설정 클래스
 * - JWT 기반 인증을 위한 설정
 * - 세션 사용 안 함 (Stateless)
 * - 폼 로그인, 로그아웃, CSRF, 기본 설정 비활성화
 * - 사용자 역할 기반 인가 처리
 */
@Configuration
@EnableWebSecurity  // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final OncePerRequestFilter jwtAuthFilter; // JWT 인증 필터 (요청마다 토큰을 검증하는 필터)

    // 인증 실패 시 처리할 커스텀 핸들러 (로그인 안 된 사용자)
    private final CustomAuthenticationEntryPointHandler customAuthenticationEntryPointHandler;

    // 인가 실패 시 처리할 커스텀 핸들러 (권한 부족한 사용자)
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    @Bean // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // HTTP 요청 경로를 객체로 매칭해주는 Matcher 빌더 등록 (패턴 기반 보안 설정용)
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        // 인증(로그인) 없이 허용할 요청 경로들, 토큰 없이도 접속 가능
        String[] permitAllWhiteURLList = {
                "/login",
                "/signup",
                "/token-refresh" // JWT 토큰 만료 시 Access Token 재발급 요청 경로
        };

        // 요청별 인가 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(permitAllWhiteURLList).permitAll() // 화이트리스트 요청 모두 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
        )
                .httpBasic(AbstractHttpConfigurer::disable) // httpBasic 비활성화
                .formLogin(AbstractHttpConfigurer::disable) //기본 로그인 폼 비활성화 (REST API로 로그인 처리할 것이기 떄문)
                .logout(AbstractHttpConfigurer::disable) // 기본 로그아웃 기능 비활성화 (클라이언트에서 처리하거나 별도 API 구성)
                .csrf(AbstractHttpConfigurer::disable) // CSRF-Cross Site Request Forgery = 교차 사이트 요청 위조
                // Spring Security의 기본값으로 CSRF 보호가 켜져 있지만 JWT 기반이므로 필요 없기 떄문에 비활성화
                
                // 세션 관리 전략 : Stateless, 세션을 생성하지 않음 (JWT방식이므로)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // 토큰을 검증하기 위한 Filter 설정
                // JWT 필터 추가 (기존 UsernamePasswordAuthenticationFilter 앞에 등록)
                // -> 사용자 이름/비밀번호 로그인 필터보다 먼저 JWT 필터가 실행되도록 함 (순서 작동하지 않으면 필터가 작동하지 않을 수 있음)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                
                // 예외 핸들링 설정 (인증 실패 / 인가 실패 시 각각 어떻게 처리할지 지정)
                .exceptionHandling(config -> config
                        .authenticationEntryPoint(customAuthenticationEntryPointHandler)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        // 최종적으로 SecurityFilterChain 반환
        return http.build();
    }
}
