package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleRepository {

    void insertArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void updateCommentCount(Long articleId, int delta);

    void updateIsDeleted(Long articleId, boolean isDeleted);

    Optional<ArticleDTO> selectArticleByIdAndUserId(Long articleId, Long userId);

    Optional<ArticleDTO> selectArticleWithRelationsById(ArticleDTO articleDTO);

    List<ArticleDTO> selectArticlesWithRelationsByRequest(ArticlePageRequest articlePageRequest);

    Long countByRequest(ArticlePageRequest articlePageRequest);

    default Page<ArticleDTO> selectPageByRequest(ArticlePageRequest articlePageRequest) {
        articlePageRequest.calculateOffset();

        return new PageImpl<>(
                selectArticlesWithRelationsByRequest(articlePageRequest),
                PageRequest.of(articlePageRequest.getPage(), articlePageRequest.getSize()),
                countByRequest(articlePageRequest)
        );
    }
}
