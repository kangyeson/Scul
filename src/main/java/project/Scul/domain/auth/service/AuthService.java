package project.Scul.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.Scul.domain.user.domain.User;
import project.Scul.domain.user.repository.UserRepository;
import project.Scul.global.security.dao.RedisDAO;
import project.Scul.global.security.jwt.JwtProvider;
import project.Scul.global.security.jwt.JwtToken;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisDAO redisDAO;

    // 회원가입 기능
    public void signUp(String accountId, String password, String name) {
        // account ID 중복 확인
        if (userRepository.existsByAccountId(accountId)) {
            throw new IllegalStateException("이미 존재하는 ID입니다.");
        }
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        // 빌더 패턴을 사용해 User 엔티티 생성
        User newUser = User.builder()
                .accountId(accountId)
                .password(encodedPassword)
                .name(name).build();

        // DB에 저장
        userRepository.save(newUser);
    }

    // 로그인 기능
    public JwtToken login(String accountId, String password) {
        // accountId로 사용자 조회
        User user = userRepository.findUserByAccountId(accountId)
                .orElseThrow(() -> new IllegalArgumentException("다시 확인해주세요."));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("다시 확인해주세요");
        }

        // Access Token 및 Refresh Token 생성
        String accessToken = jwtProvider.generateAccessToken(accountId);
        String refreshToken = jwtProvider.generateRefreshToken(accountId);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 로그아웃 기능
    public void logout(String accountId) {
        // Redis에 저장된 Refresh Token 삭제
        redisDAO.deleteValues(accountId);
    }

}
