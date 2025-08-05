package project.Scul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// JPA Repository 빈을 활성화하는 어노테이션
@EnableJpaRepositories
// 스프링 부트 애플리케이션임을 나타냄. 내부적으로 @Configuration, @EnableAutoConfiguration, @ComponentScan 포함
// SecurityAutoConfiguration을 제외하고 실행 → 스프링 시큐리티의 기본 로그인 페이지를 비활성화
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SpringSecurityJwtApplication {
    public static void main(String[] args) {
        // SpringApplication.run()을 호출하면 내장 톰캣 서버가 실행되고, 스프링 부트 애플리케이션이 시작됨
        SpringApplication.run(SpringSecurityJwtApplication.class, args);
    }
}
