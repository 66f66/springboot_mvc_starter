package f66.springboot_mvc_starter.dto;

import f66.springboot_mvc_starter.exception.InvalidRoleException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{2,12}$",
            message = "아이디는 2~12자의 영문, 숫자, 특수문자만 사용 가능합니다"
    )
    private String username;

    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 2~10자의 한글, 영문, 숫자만 사용 가능합니다"
    )
    private String nickname;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?])\\S{8,32}$",
            message = "비밀번호는 8~32자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
    )
    private String password;

    @NotEmpty(message = "필수 입력 값입니다.")
    private String passwordCheck;

    private String role;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public void setRole(String role) {

        if (!UserRoleConstants.isValidRole(role)) {

            throw new InvalidRoleException();
        }

        this.role = role;
    }
}
