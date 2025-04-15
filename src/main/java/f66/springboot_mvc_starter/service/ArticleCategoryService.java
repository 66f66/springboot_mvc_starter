package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.ArticleCategoryDTO;
import f66.springboot_mvc_starter.repository.ArticleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleCategoryService {

    private final ArticleCategoryRepository articleCategoryRepository;

    @Transactional(readOnly = true)
    public List<ArticleCategoryDTO> getCategories() {

        return articleCategoryRepository.selectCategories();
    }

    public List<ArticleCategoryDTO> addAllCategory(List<ArticleCategoryDTO> categoryDTOs) {

        long totalCount = categoryDTOs.stream()
                .mapToLong(ArticleCategoryDTO::getArticleCount)
                .sum();

        ArticleCategoryDTO allCategory = ArticleCategoryDTO.builder()
                .id(0)
                .name("전체")
                .articleCount(totalCount)
                .build();

        List<ArticleCategoryDTO> result = new ArrayList<>();
        result.add(allCategory);
        result.addAll(categoryDTOs);

        return result;
    }
}
