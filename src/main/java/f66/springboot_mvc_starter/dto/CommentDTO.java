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
public class CommentDTO {

    private Long id;

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    @Size(min = 10, max = 300, message = "댓글은 {min}자에서 {max}자 이내입니다.")
    private String content;

    private int depth;

    private boolean isDeleted;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

    private Long userId;

    private UserDTO user;

    private Long articleId;

    private ArticleDTO article;

    private Long parentCommentId;

    private CommentDTO parentComment;

    private boolean hasChildren;

    public boolean getIsDeletedHasChildren() {

        return this.hasChildren && isDeleted;
    }
}
