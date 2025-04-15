package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {

    void insertLocalUser(UserDTO userDTO);

    Optional<UserDTO> selectByUsername(String username);

    boolean existsByUsername(String username);
}
