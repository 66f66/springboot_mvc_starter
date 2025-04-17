package f66.springboot_mvc_starter.config;

import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final HttpUtil httpUtil;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (isJsonRequest) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"status\": \"FORBIDDEN\", \"message\": \"접근이 거부되었습니다.\"}");
        } else {

            response.sendRedirect("/error/403");
        }
    }
}
