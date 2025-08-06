package project.Scul.global.security.handler;

import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j // lombok의 로그 객체(log)를 자동으로 생성해주는 어노테이션
@Component

// 인증되지 않은 사용자가 요청을 보냈을 때 예외 처리를 담당하는 클래스
// AuthenticationEntryPoint는 Spring Security에서 제공하는 인터페이스로,
// 인증 실패(401 Unauthorized)에 대한 커스텀 처리를 구현할 때 사용됨
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("[CustomAuthenticationEntryPointHandler] :: {}", authException.getMessage()); // 인증 실패 메시지
        log.info("[CustomAuthenticationEntryPointHandler] :: {}", request.getRequestURI()); // 어떤 URI에서 예외가 발생했는지 확인
        log.info("[CustomAuthenticationEntryPointHandler] :: 토큰 정보가 만료되었거나 존재하지 않음");

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 응답 상태 코드를 401(Unauthorized)로 설정

        // 응답 인코딩 및 Content-Type 설정 (한글 깨짐 방지)
        response.setCharacterEncoding("UTF-8");  // "UNAUTHORIZED"
        response.setContentType("application/json; charset=UTF-8"); // 사용자에게 보이는 에러 메시지

        // JSON 형식의 에러 메시지 생성
        JsonObject returnJson = new JsonObject();
        returnJson.addProperty("errorCode", HttpStatus.UNAUTHORIZED.name()); // "UNAUTHORIZED"
        returnJson.addProperty("errorMsg", "인증되지 않은 사용자입니다.");    // 직접 메시지 설정

        PrintWriter out = response.getWriter();
        out.print(returnJson);  // // {"errorCode": "UNAUTHORIZED", "errorMsg": "인증되지 않은 사용자입니다."}
    }
}
