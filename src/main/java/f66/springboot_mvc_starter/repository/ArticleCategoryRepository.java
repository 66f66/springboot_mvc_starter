package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleCategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleCategoryRepository {

    void insertCategory(ArticleCategoryDTO articleCategoryDTO);

    void updateArticleCount(int id, long articleCount);

    Optional<ArticleCategoryDTO> selectCategoryByIdForUpdate(long id);

    List<ArticleCategoryDTO> selectCategories();

    long countCategories();
}
