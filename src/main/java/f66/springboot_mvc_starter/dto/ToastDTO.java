package f66.springboot_mvc_starter.dto;

public record ToastDTO(
        String message,
        int duration
) {

    public static ToastDTO createToast(String message) {

        return new ToastDTO(message, 5000);
    }
}
