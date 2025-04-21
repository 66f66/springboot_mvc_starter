package f66.springboot_mvc_starter.config;

import f66.springboot_mvc_starter.dto.*;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.repository.*;
import f66.springboot_mvc_starter.service.ArticleService;
import f66.springboot_mvc_starter.service.UserService;
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
    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final PasswordEncoder passwordEncoder;

    @Value("${config.admin-password}")
    private String adminPassword;

    @Value("${config.test-password}")
    private String testPassword;

    @Bean
    @Transactional
    @Profile("seed")
    public CommandLineRunner init(UserService userService) {
        return args -> {

            final String ROLE_ADMIN = "ROLE_ADMIN";

            if (userRoleRepository.countRoles() == 0) {

                UserRoleDTO roleAdmin = UserRoleDTO.builder()
                        .name(ROLE_ADMIN)
                        .displayName("관리자")
                        .build();
                UserRoleDTO roleUser = UserRoleDTO.builder()
                        .name("ROLE_USER")
                        .displayName("사용자")
                        .build();

                List<UserRoleDTO> userRoleDTOList = List.of(roleAdmin, roleUser);

                userRoleRepository.insertRoles(userRoleDTOList);
            }

            UserRoleDTO userRoleDTO = userRoleRepository.selectRoleByName(ROLE_ADMIN)
                    .orElseThrow(ResourceNotFoundException::new);

            if (userRepository.countUsers() == 0) {

                UserDTO adminUser = UserDTO.builder()
                        .username("admin")
                        .nickname("admin")
                        .password(passwordEncoder.encode(adminPassword))
                        .roleId(userRoleDTO.getId())
                        .build();

                userRepository.insertUser(adminUser);

                UserImageDTO userImageDTO = UserImageDTO.builder()
                        .userId(adminUser.getId())
                        .build();

                // admin user id = 1
                userImageRepository.insertImage(userImageDTO);

                // user id = 2,3,4,5,6
                for (int i = 1; i < 6; i++) {

                    UserDTO userDTO = UserDTO.builder()
                            .username("user" + i)
                            .nickname("u" + i)
                            .password(testPassword + i)
                            .passwordCheck(testPassword + i)
                            .build();

                    userService.createUser(userDTO);
                }
            }

            if (articleCategoryRepository.countCategories() == 0) {

                // category id = 1,2,3,4,5
                for (int i = 1; i < 6; i++) {

                    ArticleCategoryDTO articleCategoryDTO = ArticleCategoryDTO.builder()
                            .name("카테고리" + i)
                            .build();

                    articleCategoryRepository.insertCategory(articleCategoryDTO);
                }
            }

            // user id = 1(admin),2,3,4,5,6
            List<UserDTO> userDTOs = userRepository.selectUsers();

            if (articleRepository.countArticlesByRequest(new ArticlePageRequest()) == 0) {

                List<ArticleCategoryDTO> articleCategoryDTOs = articleCategoryRepository.selectCategories();

                for (int i = 0; i < articleCategoryDTOs.size(); i++) {

                    for (int j = 1; j <= 2000; j++) {

                        int rowNumber = i * 2000 + j;

                        ArticleDTO articleDTO = ArticleDTO.builder()
                                .categoryId(articleCategoryDTOs.get(i).getId())
                                .title(rowNumber + "번째 게시글입니다.")
                                .content(String.format("<p>%s번째 테스트 게시물입니다.</p>", rowNumber))
                                .build();

                        articleService.createArticle(userDTOs.get(i + 1).getId(), articleDTO);
                    }
                }
            }
        };
    }
}
