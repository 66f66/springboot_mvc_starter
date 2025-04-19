package f66.springboot_mvc_starter.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("이 작업을 수행할 수 없습니다");
    }
}
