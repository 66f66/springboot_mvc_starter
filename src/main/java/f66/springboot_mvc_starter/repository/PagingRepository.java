package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.BasePageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PagingRepository<T, R extends BasePageRequest> {

    List<T> selectList(R request);

    long count(R request);

    default Page<T> getPage(R request) {

        request.calculateOffset();

        return new PageImpl<>(
                selectList(request),
                PageRequest.of(request.getPage(), request.getSize()),
                count(request)
        );
    }
}
