package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleDTO;
import f66.springboot_mvc_starter.dto.ArticlePageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface ArticleRepository extends PagingRepository<ArticleDTO, ArticlePageRequest> {

    void insertArticle(ArticleDTO articleDTO);

    void updateArticle(ArticleDTO articleDTO);

    void updateCommentCount(Long articleId, int delta);

    void deleteArticle(Long id);

    Optional<ArticleDTO> selectArticleByIdAndUserId(Long articleId, Long userId);

    Optional<ArticleDTO> selectArticleById(Long id);

    Optional<ArticleDTO> selectArticleByIdWithVote(ArticleDTO articleDTO);
}
