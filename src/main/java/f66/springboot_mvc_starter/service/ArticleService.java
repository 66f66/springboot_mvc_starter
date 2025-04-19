package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
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

        articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), 1);
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

            articleCategoryRepository.updateArticleCount(oldArticleDTO.getCategoryId(), -1);

            articleCategoryRepository.updateArticleCount(articleDTO.getCategoryId(), 1);
        }
    }

    @Transactional
    public void deleteArticle(Long id,
                              Long currentUserId) {

        int categoryId = articleRepository
                .selectArticleByIdAndUserId(id, currentUserId)
                .orElseThrow(ForbiddenException::new)
                .getCategoryId();

        articleRepository.updateIsDeleted(id, true);

        articleCategoryRepository.updateArticleCount(categoryId, -1);
    }

    @Transactional(readOnly = true)
    public ArticleDTO getArticleByOwner(Long id,
                                        Long currentUserId) {

        return articleRepository.selectArticleByIdAndUserId(id, currentUserId)
                .orElseThrow(ForbiddenException::new);
    }

    @Transactional(readOnly = true)
    public ArticleDTO getArticleDetail(Long id,
                                       Long currentUserId) {

        return articleRepository.selectArticleWithRelationsById(id, currentUserId)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> getArticlePage(ArticlePageRequest request) {

        return articleRepository.selectPageByRequest(request);
    }
}
