package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ArticleRepository extends PagingRepository<ArticleDTO, ArticlePageRequest> {

    void insertArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void updateCommentCount(Long id, long commentCount);

    void updateDeleted(Long id, boolean deleted);

    Optional<ArticleDTO> selectArticleByIdForUpdate(Long id);

    Optional<ArticleDTO> selectArticleByIdAndUserId(Long id, Long userId);

    Optional<ArticleDTO> selectArticleWithRelationsByIdAndOptionalUserId(Long id, Long currentUserId);
}
