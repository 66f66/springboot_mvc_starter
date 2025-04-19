package f66.springboot_mvc_starter.exception;

public class WrongUsernameOrPasswordException extends RuntimeException {

    public WrongUsernameOrPasswordException() {
        super("인증 정보가 일치하지 않습니다");
    }
}
