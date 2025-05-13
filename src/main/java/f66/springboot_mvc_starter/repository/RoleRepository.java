package f66.springboot_mvc_starter.repository;

import f66.springboot_mvc_starter.dto.RoleDTO;
import f66.springboot_mvc_starter.dto.RoleType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RoleRepository {

    void insertRoles(List<RoleDTO> roleDTOS);

    Optional<RoleDTO> selectByRoleType(RoleType roleType);

    long countRoles();
}
