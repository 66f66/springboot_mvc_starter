package f66.springboot_mvc_starter.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArticlePageRequest extends BasePageRequest {

    private String searchKey;

    private String searchValue;

    private Integer categoryId;

    private Long userId;

    @Override
    protected Set<Integer> getAllowedSize() {

        return Set.of(10, 20, 50, 100);
    }

    @Override
    protected int getDefaultSize() {

        return 10;
    }
}
