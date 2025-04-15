package f66.springboot_mvc_starter.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSignInSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String redirectUrl = request.getParameter("redirect_url");

        if (redirectUrl != null && !redirectUrl.isEmpty()) {

            response.sendRedirect(redirectUrl);
        } else {

            response.sendRedirect("/");
        }
    }
}
