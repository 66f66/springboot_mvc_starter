package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.CommentDTO;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    public void createComment(Long currentUserId,
                              CommentDTO commentDTO) {

        commentDTO.setUserId(currentUserId);

        commentRepository.insertComment(commentDTO);

        articleRepository.updateCommentCount(commentDTO.getArticleId(), 1);
    }

    @Transactional
    public void updateComment(Long commentId,
                              Long currentUserId,
                              CommentDTO commentDTO) {

        commentDTO.setId(commentId);

        commentRepository.selectByIdAndUserId(commentId, currentUserId)
                .orElseThrow(ForbiddenException::new);

        commentRepository.updateComment(commentDTO);
    }

    @Transactional
    public void deleteComment(Long commentId,
                              Long currentUserId) {

        Long articleId = commentRepository.selectByIdAndUserId(commentId, currentUserId)
                .orElseThrow(ForbiddenException::new).getArticleId();

        commentRepository.updateIsDeleted(commentId, true);

        articleRepository.updateCommentCount(articleId, -1);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getComments(Long articleId) {

        return commentRepository.selectCommentsWithRelationsByArticleId(articleId);
    }
}
