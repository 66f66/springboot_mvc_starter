package f66.springboot_mvc_starter.exception;

import org.springframework.security.access.AccessDeniedException;

public class ForbiddenException extends AccessDeniedException {

    public ForbiddenException() {
        super("이 작업을 수행할 수 없습니다");
    }
}
