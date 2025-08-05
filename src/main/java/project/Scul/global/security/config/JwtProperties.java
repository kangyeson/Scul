package project.Scul.global.security.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// application.yml의 'jwt'로 시작하는 설정값들을 이 클래스 필드에 자동으로 바인딩해주는 어노테이션
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter // application.yml에 값 주입
public class JwtProperties {
    private String secretKey;
    private long accessExp;
    private long refreshExp;
    private String header;
    private String prefix;
}
