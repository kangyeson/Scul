package project.Scul.global.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
// 인가 실패 시(= 인증은 되었으나 권한이 부족한 경우) 호출되는 핸들러 클래스
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 사용자가 인증은 되었지만, 인가되지 않은 리소스에 접근할 경우 실행되는 메서드
     *
     * @param request                클라이언트의 HTTP 요청
     * @param response               클라이언트에게 보낼 HTTP 응답
     * @param accessDeniedException  발생한 인가 예외 정보
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("[CustomAccessDeniedHandler] :: {}", accessDeniedException.getMessage()); // 인가 실패 원인 메시지
        log.info("[CustomAccessDeniedHandler] :: {}", request.getRequestURL());  // 어떤 URL에서 예외가 발생했는지 확인
        log.info("[CustomAccessDeniedHandler] :: 토근 정보가 만료되었거나 존재하지 않음");

        // HTTP 403 상태 설정 (Forbidden: 권한 없음)
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // 응답 인코딩 설정 (한글 깨짐 방지)
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // 클라이언트에 전달할 JSON 응답 객체 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorCode", HttpStatus.FORBIDDEN.name());      // "FORBIDDEN"
        errorResponse.put("errorMsg", "접근이 거부되었습니다.");            // 사용자 메시지

        // Jackson ObjectMapper로 Map을 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);

        PrintWriter out = response.getWriter();
        out.print(json);
    }
}
