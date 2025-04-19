package f66.springboot_mvc_starter.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("찾을 수 없는 리소스입니다");
    }
}
