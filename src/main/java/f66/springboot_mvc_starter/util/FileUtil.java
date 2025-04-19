package f66.springboot_mvc_starter.util;

import f66.springboot_mvc_starter.exception.UserBadInputException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class FileUtil {

    /**
     * @param file         MultipartFile 타입의 파일
     * @param allowedTypes ex) {"image/jpeg", "image/png"}
     * @param maxSize      파일의 최대크기를 byte 로 나타낸 수
     * @throws UserBadInputException 파일 여부, 형식, 크기 순으로 검증
     */
    public void validateMultipartFile(MultipartFile file,
                                      String[] allowedTypes,
                                      Long maxSize) throws UserBadInputException {

        String contentType = file.getContentType();

        if (file.isEmpty()) {

            throw new UserBadInputException("파일이 없습니다");
        }

        if (contentType == null) {

            throw new UserBadInputException("유효한 파일이 아닙니다");
        }

        if (!Arrays.asList(allowedTypes).contains(contentType)) {

            throw new UserBadInputException("지원하지 않는 파일 형식입니다");
        }

        if (file.getSize() > maxSize) {

            throw new UserBadInputException("파일의 최대 허용 크기를 넘었습니다");
        }
    }
}
