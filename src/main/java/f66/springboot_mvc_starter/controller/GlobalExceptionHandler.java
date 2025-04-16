package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.util.AuthUtil;
import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpUtil httpUtil;
    private final AuthUtil authUtil;

    @ExceptionHandler({ResourceNotFoundException.class})
    public Object handleNotFoundException(Exception ex,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (!isJsonRequest) {

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.sendRedirect("/error/404");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({MethodNotAllowedException.class,
            HttpRequestMethodNotSupportedException.class,
            UnsupportedOperationException.class
    })
    public Object handleMethodNotAllowed(Exception ex,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (!isJsonRequest) {

            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.sendRedirect("/error/400");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(UserBadInputException.class)
    public Object handleUserBadInputException(UserBadInputException ex,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (!isJsonRequest) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/error/400");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidationException(MethodArgumentNotValidException ex,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (!isJsonRequest) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.sendRedirect("/error/400");
        }

        BindingResult bindingResult = ex.getBindingResult();

        List<String> errors = bindingResult.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "errors", errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({ForbiddenException.class, AccessDeniedException.class, AuthenticationException.class})
    public Object handleAuthorizationDeniedException(Exception ignoredE,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);
        boolean isAnonymousUser = !authUtil.isAuthenticated();

        if (!isJsonRequest) {

            String redirect;

            if (isAnonymousUser) {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                redirect = "/auth/sign-in?redirect_url" + URLEncoder.encode(httpUtil.getRequestURIWithQuery(request), StandardCharsets.UTF_8);
            } else {

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                redirect = "/error/403";
            }

            response.sendRedirect(redirect);
        }

        if (isAnonymousUser) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
