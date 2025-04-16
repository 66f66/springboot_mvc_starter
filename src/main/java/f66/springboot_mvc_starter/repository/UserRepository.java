package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserRepository {

    void insertLocalUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    boolean existsByUsername(String username);

    Optional<UserDTO> selectById(Long id);

    Optional<UserDTO> selectByUsername(String username);
}
