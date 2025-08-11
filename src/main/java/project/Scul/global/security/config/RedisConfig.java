package project.Scul.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories  // Redis 저장소 기능 활성화
public class RedisConfig {
    private String host = "redis.xquare.app";
    private int port = 6379;

    // Redis 연결 팩토리 설정
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Redis 설정 - host와 port가 필요함
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        
        // Lettuce 라이브러리를 사용해서 Redis에 연결 (Lettuce : Redis의 클라이언트 라이브러리)
        // Jedis보다 성능이 좋고 비동기 처리가 가능하므로 Lettuce 선택
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    // RedisTemplate 설정 : Redis와의 통신을 처리하는 핵심적인 객체
    // RedisTemplate는 Redis 서버에 데이터를 읽고 쓰는 작업 처리 -> Set, Get, Delete, Update 등의 작업을 할 수 있도록 해줌
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // RedisTemplate는 트랜잭션을 지원함
        // 트랜잭션 안에서 오류가 발생하면 그 작업을 모두 취소함 (롤백)

        // Redis와 통신할 때 사용할 템플릿 설정, Redis 서버와의 실제 연결을 담당
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // key, value에 대한 직렬화 방법 설정
        // Redis에 데이터를 저장할 때 문자열 형태로 저장, 값을 읽어올 때도 문자열로 역직렬화됨
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        // hash key, hash value에 대한 직렬화 방법 설정
        // -> 문자열로 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
