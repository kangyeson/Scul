package project.Scul.global.security.dao;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

// Redis 데이터 접근을 위한 클래스
@Component
public class RedisDAO {
    // RedisTemplate : Redis와 상호작용하기 위한 핵심 클래스
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisDAO(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 만료 시간이 있는 데이터 저장 (Duration : 만료시간 설정)
    // 주로 RefreshToken 저장할 때 사용함
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data, duration);
    }

    // 데이터를 조회하는 메서드
    // RefreshToken 검증 시 사용함
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        Object result = values.get(key);
        // 조회된 데이터가 String 타입이 아닐 수 있으므로 안전하게 형 변환
        return (result instanceof String) ? (String) result : null;
    }

    // 데이터 삭제
    // 로그아웃 시 RefreshToken을 삭제할 때 사용함
    public void deleteValues(String key){
        redisTemplate.delete(key);
    }
}
