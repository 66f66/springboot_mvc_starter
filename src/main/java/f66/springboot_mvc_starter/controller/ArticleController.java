package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.*;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.service.ArticleCategoryService;
import f66.springboot_mvc_starter.service.ArticleService;
import f66.springboot_mvc_starter.service.ArticleVoteService;
import f66.springboot_mvc_starter.service.CommentService;
import f66.springboot_mvc_starter.util.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final HttpUtil httpUtil;
    private final ArticleService articleService;
    private final ArticleVoteService articleVoteService;
    private final ArticleCategoryService articleCategoryService;
    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/vote")
    public ResponseEntity<Map<String, VoteResult>> vote(@AuthenticationPrincipal CustomUserDetails user,
                                                        @PathVariable Long id) {

        VoteResult result = articleVoteService.voteArticle(id, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form")
    public String articleForm(@AuthenticationPrincipal CustomUserDetails user,
                              @RequestParam(required = false) Long id,
                              Model model) {

        if (id == null) {

            model.addAttribute("articleDTO", new ArticleDTO());
        } else {

            ArticleDTO articleDTO = articleService
                    .getArticleByIdAndUserId(id, user.getId())
                    .orElseThrow(ForbiddenException::new);

            if (!Objects.equals(articleDTO.getUserId(), user.getId())) {

                throw new ForbiddenException();
            }

            model.addAttribute("articleDTO", articleDTO);
        }

        List<ArticleCategoryDTO> articleCategoryDTOs = articleCategoryService.getCategories();

        model.addAttribute("categoryDTOs", articleCategoryDTOs);

        return "article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Map<String, Long>> createOrUpdateArticle(@AuthenticationPrincipal CustomUserDetails user,
                                                                   @RequestBody @Valid ArticleDTO articleDTO) {

        if (articleDTO.getId() != null) {

            ArticleDTO oldArticleDTO = articleService
                    .getArticleByIdAndUserId(articleDTO.getId(), user.getId())
                    .orElseThrow(ForbiddenException::new);

            articleService.updateArticle(oldArticleDTO, articleDTO);

            return ResponseEntity.ok(Map.of("id", articleDTO.getId()));
        }

        articleService.createArticle(user.getId(), articleDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", articleDTO.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public String deleteArticle(@AuthenticationPrincipal CustomUserDetails user,
                                @PathVariable Long id,
                                RedirectAttributes redirectAttributes) {

        ArticleDTO articleDTO = articleService.getArticleByIdAndUserId(id, user.getId())
                .orElseThrow(ForbiddenException::new);

        articleService.deleteArticle(id, articleDTO.getCategoryId());

        redirectAttributes.addFlashAttribute("toast",
                ToastDTO.createToast("게시물을 삭제했습니다."));

        return "redirect:/article";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable Long id,
                       Model model) {

        model.addAttribute("articleDTO", articleService.getArticleByIdWithVote(id));
        model.addAttribute("commentDTOs", commentService.getComments(id));
        model.addAttribute("commentDTO", new CommentDTO());

        return "article_view";
    }

    @GetMapping
    public String list(HttpServletRequest request,
                       ArticlePageRequest articlePageRequest,
                       Model model) {

        Page<ArticleDTO> paging = articleService.getArticlePage(articlePageRequest);
        List<ArticleCategoryDTO> categoryDTOs = articleCategoryService.addAllCategory(articleCategoryService.getCategories());

        model.addAttribute("paging", paging);
        model.addAttribute("articleDTOs", paging.getContent());
        model.addAttribute("categoryDTOs", categoryDTOs);
        model.addAttribute("articlePageRequest", articlePageRequest);
        model.addAttribute("requestURIWithQuery", httpUtil.getRequestURIWithQuery(request));

        return "article_list";
    }
}
