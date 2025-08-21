package project.Scul.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.Scul.domain.user.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserId(Long userId);

    // accountId로 사용자 존재 여부 확인 (회원 가입시 중복 아이디 체크)
    boolean existsByAccountId(String accountId);

    // accountId로 사용자 검색 (로그인 시 사용자 정보 조회)
    Optional<User> findUserByAccountId(String accountId);
}
