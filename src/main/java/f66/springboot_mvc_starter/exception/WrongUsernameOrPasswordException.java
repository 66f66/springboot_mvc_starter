package f66.springboot_mvc_starter.exception;

public class WrongUsernameOrPasswordException extends RuntimeException {

    public WrongUsernameOrPasswordException() {
        super("Can't login with credentials");
    }
}
