package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.CommentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentRepository {

    void insertComment(CommentDTO commentDTO);

    void updateComment(CommentDTO commentDTO);

    void deleteComment(Long id);

    Optional<CommentDTO> selectByIdAndUserId(Long commentId, Long userId);

    Optional<CommentDTO> selectById(Long commentId);

    List<CommentDTO> selectCommentsByArticleId(Long articleId);
}
