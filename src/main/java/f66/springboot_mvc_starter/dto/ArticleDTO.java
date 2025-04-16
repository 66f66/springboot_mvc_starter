package f66.springboot_mvc_starter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

    private Long id;

    @NotBlank(message = "제목은 필수 값입니다.")
    @Size(min = 2, max = 100, message = "제목은 {min}자 이상 {max}자 이내입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 값입니다.")
    @Size(min = 10, max = 2000, message = "내용은 {min}자 이상 {max}자 이내입니다.")
    private String content;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private int categoryId;

    private ArticleCategoryDTO category;

    private Long userId;

    private UserDTO user;

    private Boolean voted;

    private int voteCount;

    private int commentCount;
}
