package f66.springboot_mvc_starter.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "아이디는 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{2,12}$",
            message = "아이디는 2~12자의 영문, 숫자, 특수문자만 사용 가능합니다"
    )
    private String username;

    @NotBlank(message = "닉네임은 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 2~10자의 한글, 영문, 숫자만 사용 가능합니다"
    )
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력값입니다", groups = ValidationGroups.Create.class)
    @Pattern(
            regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{8,12}$",
            message = "비밀번호는 8~32자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력값입니다", groups = ValidationGroups.Create.class)
    private String passwordCheck;

    @Pattern(
            regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{8,12}$",
            message = "비밀번호는 8~12자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
    )
    private String oldPassword;

    @Pattern(
            regexp = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\",./<>?]{8,12}$",
            message = "비밀번호는 8~12자이며, 영문 대/소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다"
    )
    private String newPassword;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long roleId;

    private UserRoleDTO role;

    private Long imageId;

    private UserImageDTO image = new UserImageDTO();
}
