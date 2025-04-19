package f66.springboot_mvc_starter.exception;

public class UserBadInputException extends RuntimeException {

    public UserBadInputException(String message) {
        super(message);
    }
}
