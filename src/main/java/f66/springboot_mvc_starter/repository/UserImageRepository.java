package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserImageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserImageRepository {

    void insertUserImage(UserImageDTO userImageDTO);

    void updateUserImage(UserImageDTO userImageDTO);

    Optional<UserImageDTO> selectByUserId(Long userId);
}
