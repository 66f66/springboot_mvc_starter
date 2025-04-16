package f66.springboot_mvc_starter.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import f66.springboot_mvc_starter.dto.CloudinaryUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {

    private final Cloudinary cloudinary;

    public CloudinaryUploadResult uploadFile(MultipartFile file, Map<String, String> options) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Map cloudinaryUploadResult = cloudinary.uploader().upload(file.getBytes(), options);

        return objectMapper.convertValue(cloudinaryUploadResult, CloudinaryUploadResult.class);
    }

    public void deleteFile(String publicId) throws IOException {

        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
