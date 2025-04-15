package f66.springboot_mvc_starter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCategoryDTO {

    private int id;

    @NotBlank(message = "공백일 수 없습니다.")
    private String name;

    private Long articleCount;
}
