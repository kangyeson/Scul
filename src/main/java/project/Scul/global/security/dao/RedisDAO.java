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

    // ValueOperations : Redis의 key-value 데이터 구조에서 값을 쉽게 저장하고 조회할 수 있도록 돕는 메서드 제공
    private final ValueOperations<String, Object> values;

    public RedisDAO(RedisTemplate<String, Object> redisTemplate, ValueOperations<String, Object> values) {
        this.redisTemplate = redisTemplate;
        this.values = redisTemplate.opsForValue(); // string 타입을 쉽게 처리하는 메서드
    }

    // 기본 데이터 저장
    // Redis에 기본적으로 만료 시간 없이 데이터 저장
    public void setValue(String key, String data) {
        values.set(key, data);
    }

    // 만료 시간이 있는 데이터 저장 (Duration : 만료시간 설정)
    // 주로 RefreshToken 저장할 때 사용함
    public void setValue(String key, String data, Duration duration) {
        values.set(key, data, duration);
    }

    // 데이터 조회
    // RefreshToken 검증 시 사용됨
    public Object getValue(String key) {
        return values.get(key);
    }

    // 데이터 삭제
    // 로그아웃 시 RefreshToken을 삭제할 때 사용함
    public void deleteValues(String key){
        redisTemplate.delete(key);
    }
}
