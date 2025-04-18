package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.ArticleCategoryDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleCategoryRepository {

    void updateArticleCount(int id, int delta);

    List<ArticleCategoryDTO> selectCategories();
}
