package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.CommentDTO;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.repository.ArticleCategoryRepository;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void deleteArticle(Long id) {

        ArticleDTO articleDTO = articleRepository.selectArticleById(id)
                .orElseThrow(ResourceNotFoundException::new);

        articleRepository.updateIsDeleted(id, true);

        articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), -1);
    }

    @Transactional
    public void deleteComment(Long id) {

        CommentDTO commentDTO = commentRepository.selectCommentById(id)
                .orElseThrow(ResourceNotFoundException::new);

        commentRepository.updateIsDeleted(id, true);

        articleRepository.updateCommentCount(commentDTO.getArticleId(), -1);
    }
}
