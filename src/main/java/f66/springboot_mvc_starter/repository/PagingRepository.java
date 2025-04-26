package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.BasePageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PagingRepository<T, R extends BasePageRequest> {

    List<T> selectListForPage(R request);

    long countForPage(R request);

    default Page<T> getPage(R request) {

        request.calculateOffset();

        return new PageImpl<>(
                selectListForPage(request),
                PageRequest.of(request.getPage(), request.getSize()),
                countForPage(request)
        );
    }
}
