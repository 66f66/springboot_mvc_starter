package f66.springboot_mvc_starter.security;

import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HttpUtil httpUtil;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (isJsonRequest) {

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\": \"UNAUTHORIZED\", \"message\": \"인증이 필요합니다.\"}");
        } else {

            String redirect = "/auth/sign-in?redirect_url=" + URLEncoder.encode(httpUtil.getRequestURIWithQuery(request), StandardCharsets.UTF_8);

            response.sendRedirect(redirect);
        }
    }
}
