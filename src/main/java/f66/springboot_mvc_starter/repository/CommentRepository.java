package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentRepository {

    void insertComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void updateIsDeleted(Long id, boolean isDeleted);

    Optional<CommentDTO> selectCommentById(Long id);

    Optional<CommentDTO> selectCommentByIdAndUserId(Long id, Long userId);

    List<CommentDTO> selectCommentsWithRelationsByArticleId(Long articleId);
}
