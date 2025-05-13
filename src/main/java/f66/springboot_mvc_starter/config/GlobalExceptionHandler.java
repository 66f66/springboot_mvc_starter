package f66.springboot_mvc_starter.config;

import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.util.AuthUtil;
import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpUtil httpUtil;
    private final AuthUtil authUtil;

    @ExceptionHandler(EntityNotFoundException.class)
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

    @ExceptionHandler(MyBatisSystemException.class)
    public Object handleMyBatisSystemException(MyBatisSystemException ex,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        if (!isJsonRequest) {

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/error/500");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
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

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {

        boolean isJsonRequest = httpUtil.isJsonRequest(request);

        System.out.println("Hit5");

        log.error(e.getMessage());

        if (!isJsonRequest) {

            System.out.println("Hit6");

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.sendRedirect("/error/500");

            return null;
        }

        System.out.println("Hit7");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
