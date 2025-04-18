package f66.springboot_mvc_starter.config;

import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserImageDTO;
import f66.springboot_mvc_starter.dto.UserRoleDTO;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.repository.UserImageRepository;
import f66.springboot_mvc_starter.repository.UserRepository;
import f66.springboot_mvc_starter.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultInitializer {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${config.default-image-url}")
    private String defaultImageUrl;

    @Value("${config.admin-password}")
    private String adminPassword;

    @Bean
    @Transactional
    @Profile("seed")
    public CommandLineRunner init() {
        return args -> {

            final String ROLE_ADMIN = "ROLE_ADMIN";

            if (userRoleRepository.countRoles() == 0) {

                UserRoleDTO roleUser = new UserRoleDTO();
                UserRoleDTO roleAdmin = UserRoleDTO.builder()
                        .name(ROLE_ADMIN)
                        .displayName("관리자")
                        .build();

                List<UserRoleDTO> userRoleDTOList = List.of(roleAdmin, roleUser);

                userRoleRepository.insertRoles(userRoleDTOList);
            }

            UserRoleDTO userRoleDTO = userRoleRepository.selectRoleByName(ROLE_ADMIN)
                    .orElseThrow(ResourceNotFoundException::new);

            if (userRepository.countUsers() == 0) {

                UserDTO user = UserDTO.builder()
                        .username("admin")
                        .nickname("admin")
                        .password(passwordEncoder.encode(adminPassword))
                        .roleId(userRoleDTO.getId())
                        .build();

                userRepository.insertUser(user);

                UserImageDTO userImageDTO = UserImageDTO.builder()
                        .userId(user.getId())
                        .build();

                userImageRepository.insertImage(userImageDTO);
            }
        };
    }
}
