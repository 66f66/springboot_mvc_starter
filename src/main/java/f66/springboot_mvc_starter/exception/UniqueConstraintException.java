package f66.springboot_mvc_starter.exception;

public class UniqueConstraintException extends RuntimeException {

    public UniqueConstraintException() {
        super("Unique constraint found");
    }

    public UniqueConstraintException(String message) {
        super(message);
    }
}
