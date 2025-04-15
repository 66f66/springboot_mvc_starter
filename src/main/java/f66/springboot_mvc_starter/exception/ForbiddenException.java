package f66.springboot_mvc_starter.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Forbidden access");
    }

    public ForbiddenException(String message) {

        super(message);
    }
}
