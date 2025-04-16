package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.repository.ArticleCategoryRepository;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final AuthUtil authUtil;

    @Transactional
    public void createArticle(Long currentUserId,
                              ArticleDTO articleDTO) {

        articleDTO.setUserId(currentUserId);

        articleRepository.insertArticle(articleDTO);

        articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), 1);
    }

    @Transactional
    public void updateArticle(ArticleDTO oldArticleDTO,
                              ArticleDTO articleDTO) {

        if (!Objects.equals(oldArticleDTO.getCategoryId(), articleDTO.getCategoryId())) {

            articleCategoryRepository.updateArticleCount(oldArticleDTO.getCategoryId(), -1);

            articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), 1);
        }

        articleRepository.updateArticle(articleDTO);
    }

    @Transactional
    public void deleteArticle(Long id,
                              int categoryId) {

        articleRepository.deleteArticle(id);

        articleCategoryRepository.updateArticleCount(categoryId, -1);
    }

    @Transactional(readOnly = true)
    public Optional<ArticleDTO> getArticleById(Long articleId) {

        return articleRepository.selectArticleById(articleId);
    }

    @Transactional(readOnly = true)
    public Optional<ArticleDTO> getArticleByIdAndUserId(Long id,
                                                        Long currentUserId) {

        return articleRepository.selectArticleByIdAndUserId(id, currentUserId);
    }

    @Transactional(readOnly = true)
    public ArticleDTO getArticleByIdWithVote(Long id) {

        ArticleDTO articleDTO = ArticleDTO.builder()
                .id(id)
                .build();

        authUtil.currentUserId().ifPresent(articleDTO::setUserId);

        return articleRepository.selectArticleByIdWithVote(articleDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> getArticlePage(ArticlePageRequest request) {

        return articleRepository.getPage(request);
    }
}
