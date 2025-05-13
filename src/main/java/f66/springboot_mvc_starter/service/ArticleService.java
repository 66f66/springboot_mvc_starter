package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleCategoryDTO;
import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.repository.ArticleCategoryRepository;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCategoryRepository articleCategoryRepository;

    @Transactional
    public void createArticle(Long currentUserId,
                              ArticleDTO articleDTO) {

        articleDTO.setUserId(currentUserId);

        articleRepository.insertArticle(articleDTO);

        ArticleCategoryDTO articleCategoryDTO = articleCategoryRepository
                .selectCategoryByIdForUpdate(articleDTO.getCategoryId())
                .orElseThrow();

        articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), articleCategoryDTO.getArticleCount() + 1);
    }

    @Transactional
    public void updateArticle(Long id,
                              Long currentUserId,
                              ArticleDTO articleDTO) {

        ArticleDTO oldArticleDTO = articleRepository
                .selectArticleByIdAndUserId(id, currentUserId)
                .orElseThrow(ForbiddenException::new);

        articleRepository.updateArticle(articleDTO);

        if (!Objects.equals(oldArticleDTO.getCategoryId(), articleDTO.getCategoryId())) {

            ArticleCategoryDTO oldArticleCategoryDTO = articleCategoryRepository
                    .selectCategoryByIdForUpdate(oldArticleDTO.getCategoryId())
                    .orElseThrow();

            articleCategoryRepository.updateArticleCount(oldArticleDTO.getCategoryId(), oldArticleCategoryDTO.getArticleCount() - 1);

            ArticleCategoryDTO articleCategoryDTO = articleCategoryRepository
                    .selectCategoryByIdForUpdate(articleDTO.getCategoryId())
                    .orElseThrow();

            articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), articleCategoryDTO.getArticleCount() + 1);
        }
    }

    @Transactional
    public void deleteArticle(Long id,
                              Long currentUserId) {

        int categoryId = articleRepository
                .selectArticleByIdAndUserId(id, currentUserId)
                .orElseThrow(ForbiddenException::new)
                .getCategoryId();

        articleRepository.updateDeleted(id, true);

        ArticleCategoryDTO articleCategoryDTO = articleCategoryRepository
                .selectCategoryByIdForUpdate(categoryId)
                .orElseThrow();

        articleCategoryRepository.updateArticleCount(categoryId, articleCategoryDTO.getArticleCount() - 1);
    }

    @Transactional(readOnly = true)
    public ArticleDTO getArticleByOwner(Long id,
                                        Long currentUserId) {

        return articleRepository
                .selectArticleByIdAndUserId(id, currentUserId)
                .orElseThrow(ForbiddenException::new);
    }

    @Transactional(readOnly = true)
    public ArticleDTO getArticleDetail(Long id,
                                       Long currentUserId) {

        return articleRepository
                .selectArticleWithRelationsByIdAndOptionalUserId(id, currentUserId)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> getArticlePage(ArticlePageRequest request) {

        return articleRepository.getPage(request);
    }
}
