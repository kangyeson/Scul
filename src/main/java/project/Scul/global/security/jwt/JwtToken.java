package project.Scul.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// 빌터 패턴을 활성화하여, 객체 생성 시 코드의 가독성을 높여줌
@Builder
/*
JwtToken token = JwtToken.builder()
        .grantType("Bearer")
        .accessToken("access_token_value")
        .refreshToken("refresh_token_value")
        .build();
*/

// 모든 필드를 매개변수로 받는 생성자를 자동으로 생성
@AllArgsConstructor // new JwtToken("Bearer", "access_token", "refresh_token")

// @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor을 모두 포함
@Data
public class JwtToken {
    private String grantType; // JWT에 대한 인증 타입 지정, Bearer 이증 방식 사용할 예정
    private String accessToken;
    private String refreshToken;
}
