package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final HttpUtil httpUtil;

    @ModelAttribute("contextRequestURIWithQuery")
    public String contextCurrentPath(HttpServletRequest request) {

        return httpUtil.getRequestURIWithQuery(request);
    }
}
