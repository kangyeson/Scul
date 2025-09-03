package project.Scul.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.Scul.global.security.config.JwtProperties;
import project.Scul.global.security.dao.RedisDAO;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@RequiredArgsConstructor // final 키워드가 붙은 필드 생성자 자동 생성
@Slf4j // lombok의 로그 객체(log)를 자동으로 생성해주는 어노테이션
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final RedisDAO redisDAO; // RefreshToken 저장을 위해 Redis 사용

    private SecretKey key; // JWT 서명을 위한 SecretKey 객체 = 서명키(Signature)

    // Bean 초기화 직후 실행되는 메서드 (단일 책임 원칙)
    // 설정된 secretKey 문자열을 바이트 배열로 변환하여 서명 키를 생성함
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(jwtProperties.getSecretKey().getBytes());
        key = Keys.hmacShaKeyFor(keyBytes); // 키 초기화
    }

    /**
     * Access Token 생성 메서드
     * - 사용자 식별자(accountId)를 기반으로 Access Token 생성
     * - 프로젝트 특성에 따라 권한(auth) 클레임을 포함시키지 않음
     * - Access Token의 유효시간은 application.yml의 설정값에 따름
     */
    public String generateAccessToken(final String accountId) {
        return Jwts.builder()
                .setSubject(accountId) // 토큰의 주체(subject)에 accountId 설정
                .setIssuedAt(new Date()) // 토큰 발급 시각
                // 만료 시각
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExp())) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 키 설정 (JWT 토큰에 서명을 붙임, 신뢰성 생성)
                .compact(); // 최종 JWT 문자열로 변환하여 반환 (header.payload.signature 구조)
    }

    /**
     * Refresh Token 생성 메서드
     * - 사용자 식별자(accountId)를 기반으로 Refresh Token 생성
     * - Refresh Token의 유효시간은 Access보다 길게 설정
     */
    public String generateRefreshToken(final String accountId) {
        // Refresh Token을 Redis에 저장
        String refreshToken = Jwts.builder()
                .setSubject(accountId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getRefreshExp()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisDAO.setValues(accountId, refreshToken, Duration.ofDays(jwtProperties.getRefreshExp()));
        return refreshToken;
    }

    /**
     * 토큰 내부의 모든 Claims 정보 추출
     * 만료된 토큰에서도 클레임 정보를 가져올 수 있도록 예외처리를 포함함
     */
    private Claims getAllClaimsFromToken(final String token) {
        try {
            return Jwts.parserBuilder() // JWT를 해석할 Parser(파서)
                    .setSigningKey(key) // 서명키(key)가 JWT를 만들 때 사용한 것과 똑같은지(Signature와 동일한지) 검증
                    .build() // 설정을 마친 Parser를 생성
                    .parseClaimsJws(token)  // 전달받은 JWT 토큰을
                    // 1. 디코딩하고
                    // 2. Header, Payload, Signature로 분리한 뒤
                    // 3. Signature(서명)이 일치하는지 검증하고
                    // 4. 최종적으로 Claims(내용물, 사용자 정보 등)을 포함한 객체를 반환
                    .getBody(); // payload(Claims) 반환
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰에서 사용자 ID(subject) 추출
     */
    public String getAccountIdFromToken(final String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 토큰에서 만료일자 추출 ->  만료 전 자동 갱신 로직에 사용
     */
    public Date getExpirationDateFromToken(final String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * 토큰에서 특정 Claim (사용자 속성 정보) 추출
     *
     * @param token JWT
     * @param claimsResolver Get Function With Target Claim
     * @param <T> Target Claim
     * @return 사용자 속성 정보
     */
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        if (!validateToken(token)) return null; // token 유효성 검증
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims); // claimsResolver : Claims객체를 입력받아 원하는 타입의 정보를 반환하는 함수
                                            // 어떤 값을 받냐에 따라 내부적으로 다르게 실행
                                            // getClaimFromToken(token, Claims::getSubject); => subject 값 반환
                                            // getClaimFromToken(token, Claims::getExpiration); => 만료 시간 반환
    }

    /**
     * Refresh Token 유효성 검증
     * - Redis에 저장된 토큰과 비교하여 유효성 검증
     */
    private boolean validateRefreshToken(String token) {
        if (!validateToken(token)) return false;

        try {
            // token에서 accountId 추출
            String accountId = getAccountIdFromToken(token);
            // Redis에 저장된 RefreshToken과 비교
            String redisToken = (String) redisDAO.getValues(accountId);
            return token.equals(redisToken);
        } catch (Exception e) {
            log.info("RefreshToken Validation Failed", e);
            return false;
        }

    }

    /**
     * 토큰 유효성 검증 메서드
     * - 토큰이 올바른 서명으로 만들어졌는지, 만료되지 않았는지를 확인함
     * - 예외 발생 시 false 반환
     */
    public boolean validateToken(final String token) {
        try{
            Jwts.parserBuilder() // JWT를 해석할 Parser(파서)
                    .setSigningKey(key) // 서명키(key)가 JWT를 만들 때 사용한 것과 똑같은지(Signature와 동일한지) 검증
                    .build() // 설정을 마친 Parser를 생성
                    .parseClaimsJws(token); // 전달받은 JWT 토큰으로 Claims 반환
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
