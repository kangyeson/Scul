package project.Scul.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import project.Scul.domain.auth.controller.dto.request.LoginRequest;
import project.Scul.domain.auth.controller.dto.request.SignUpRequest;
import project.Scul.domain.auth.service.AuthService;
import project.Scul.global.security.jwt.JwtProvider;
import project.Scul.global.security.jwt.JwtToken;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    // 회원가입 요청을 처리하는 엔드포인트
    @PostMapping("/signup")
    // @RequestBody SignUpRequest signUpRequest: 클라이언트가 보낸 JSON 데이터를 SignUpRequest 객체에 자동으로 매핑
    // SignUpRequest는 accountId, password, name을 포함하는 DTO
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequest signUpRequest) {
        // 클라이언트로부터 받은 회원가입 정보를 AuthService로 전달
        authService.signUp(signUpRequest.getAccountId(), signUpRequest.getPassword(), signUpRequest.getName());

        // 회원가입 성공 시 200 OK 상태 코드를 반환
        return ResponseEntity.ok().build();
    }

    // 로그인 요청을 처리하는 엔드포인트
    @PostMapping("/login")
    // @RequestBody LoginRequest loginRequest: 클라이언트가 보낸 JSON 데이터를 LoginRequest 객체에 자동으로 매핑
    public ResponseEntity<JwtToken> login(@RequestBody LoginRequest loginRequest) {
        // 클라이언트로부터 받은 로그인 정보를 AuthService로 전달
        // AuthService의 login 메서드로부터 JWT 토큰을 반환받음
        JwtToken token = authService.login(loginRequest.getAccountId(), loginRequest.getPassword());

        // JwtToken객체를 200 OK 상태 코드와 함께 클라이언트에게 반환
        return ResponseEntity.ok(token);
    }

    // 로그아웃 요청을 처리하는 엔드포인트
    // 클라이언트가 로그아웃 요청을 보낼 때, HTTP 헤더의 Authorization 필드에 Access Token을 담아 보냄
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        // 헤더에서 토큰 추출 -> "Bearer " 접두사 제거
        String accessToken = authorizationHeader.substring(7);

        // WT 토큰에서 accountId 추출
        String accountId = jwtProvider.getAccountIdFromToken(accessToken);

        // 로그아웃 처리 (Redis 토큰 삭제)
        authService.logout(accountId);
        return ResponseEntity.ok().build();
    }
}
