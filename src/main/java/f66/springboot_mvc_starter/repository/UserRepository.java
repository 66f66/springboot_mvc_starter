package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRepository {

    void insertUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    boolean existsByUsername(String username);

    Optional<UserDTO> selectUserByUsername(String username);

    Optional<UserDTO> selectUserWithRelationsById(Long id);

    Optional<UserDTO> selectUserWithRelationsByUsername(String username);

    List<UserDTO> selectUsers();

    long countUsers();
}
