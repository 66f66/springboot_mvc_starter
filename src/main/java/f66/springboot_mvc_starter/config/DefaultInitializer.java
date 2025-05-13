package f66.springboot_mvc_starter.config;

import f66.springboot_mvc_starter.dto.*;
import f66.springboot_mvc_starter.repository.ArticleCategoryRepository;
import f66.springboot_mvc_starter.repository.ArticleRepository;
import f66.springboot_mvc_starter.repository.RoleRepository;
import f66.springboot_mvc_starter.repository.UserRepository;
import f66.springboot_mvc_starter.service.ArticleService;
import f66.springboot_mvc_starter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ArticleCategoryRepository articleCategoryRepository;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Value("${config.admin-password}")
    private String adminPassword;

    @Bean
    @Transactional
    @Profile("seed")
    public CommandLineRunner init(UserService userService) {
        return args -> {

            if (roleRepository.countRoles() == 0) {

                RoleDTO roleAdmin = RoleDTO.builder()
                        .roleType(RoleType.ROLE_ADMIN)
                        .displayName("관리자")
                        .build();
                RoleDTO roleUser = RoleDTO.builder()
                        .roleType(RoleType.ROLE_USER)
                        .displayName("사용자")
                        .build();

                List<RoleDTO> roleDTOList = List.of(roleAdmin, roleUser);

                roleRepository.insertRoles(roleDTOList);
            } else {

                return;
            }

            if (userRepository.countUsers() == 0) {

                UserDTO adminUser = UserDTO.builder()
                        .username("admin")
                        .nickname("admin")
                        .password(adminPassword)
                        .passwordCheck(adminPassword)
                        .build();

                userService.createUser(adminUser, RoleType.ROLE_ADMIN);

                // user id = 2,3,4,5,6
                for (int i = 1; i < 6; i++) {

                    UserDTO userDTO = UserDTO.builder()
                            .username("user" + i)
                            .nickname("u" + i)
                            .password("12341234" + i)
                            .passwordCheck("12341234" + i)
                            .build();

                    userService.createUser(userDTO, RoleType.ROLE_USER);
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

            if (articleRepository.countForPage(new ArticlePageRequest()) == 0) {

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
