package f66.springboot_mvc_starter.util;

import f66.springboot_mvc_starter.exception.UserBadInputException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class FileUtil {

    public void validateFile(MultipartFile file,
                             String[] allowedTypes,
                             Long maxSize) throws UserBadInputException {

        String contentType = file.getContentType();

        if (!file.isEmpty()
                && contentType != null && Arrays.asList(allowedTypes).contains(contentType.toLowerCase())
                && file.getSize() <= maxSize) {

            return;
        }

        throw new UserBadInputException();
    }
}
