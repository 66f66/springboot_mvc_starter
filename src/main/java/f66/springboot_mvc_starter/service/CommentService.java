package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.CommentDTO;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    public void updateComment(CommentDTO commentDTO) {

        commentRepository.updateComment(commentDTO);
    }

    @Transactional
    public void deleteComment(Long id,
                              Long articleId) {

        commentRepository.deleteComment(id);

        articleRepository.updateCommentCount(articleId, -1);
    }

    @Transactional(readOnly = true)
    public Optional<CommentDTO> getCommentById(Long id) {

        return commentRepository.selectById(id);
    }

    @Transactional(readOnly = true)
    public Optional<CommentDTO> getCommentByIdAndUserId(Long id,
                                                        Long currentUserId) {

        return commentRepository.selectByIdAndUserId(id, currentUserId);
    }

    @Transactional(readOnly = true)
    public List<CommentDTO> getComments(Long articleId) {

        return commentRepository.selectCommentsByArticleId(articleId);
    }
}
