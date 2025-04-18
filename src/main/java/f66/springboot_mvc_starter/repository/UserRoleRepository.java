package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.UserRoleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserRoleRepository {

    void insertRoles(List<UserRoleDTO> userRoleDTOs);

    Optional<UserRoleDTO> selectRoleByName(String name);

    long countRoles();
}
