package f66.springboot_mvc_starter.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException() {
        super("Invalid role accepted");
    }
}
