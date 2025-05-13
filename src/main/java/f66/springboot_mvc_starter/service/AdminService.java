package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleCategoryDTO;
import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.CommentDTO;
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

        ArticleDTO articleDTO = articleRepository.selectArticleByIdForUpdate(id)
                .orElseThrow();

        articleRepository.updateDeleted(id, true);

        ArticleCategoryDTO articleCategoryDTO = articleCategoryRepository
                .selectCategoryByIdForUpdate(articleDTO.getCategoryId())
                .orElseThrow();

        articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), articleCategoryDTO.getArticleCount() - 1);
    }

    @Transactional
    public void deleteComment(Long id) {

        CommentDTO commentDTO = commentRepository
                .selectCommentById(id)
                .orElseThrow();

        commentRepository.updateDeleted(id, true);

        ArticleDTO articleDTO = articleRepository
                .selectArticleByIdForUpdate(commentDTO.getArticleId())
                .orElseThrow();

        articleRepository.updateCommentCount(commentDTO.getArticleId(), articleDTO.getCommentCount() - 1);
    }
}
