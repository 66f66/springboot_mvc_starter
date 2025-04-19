package f66.springboot_mvc_starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserImageDTO {

    private Long id;

    private String publicId;

    private String originalFileName;

    private String url;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private Long userId;

    private MultipartFile file;
}
