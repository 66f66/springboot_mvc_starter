package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserImageRepository {

    void insertImage(UserImageDTO userImageDTO);

    void updateImage(UserImageDTO userImageDTO);

    Optional<UserImageDTO> selectImageByUserId(Long userId);
}
