package f66.springboot_mvc_starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    private Long id;

    private RoleType roleType;

    private String displayName;
}
