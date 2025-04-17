package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.*;
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
    @PostMapping("/{articleId}/vote")
    public ResponseEntity<Map<String, VoteResult>> vote(@AuthenticationPrincipal CustomUserDetails user,
                                                        @PathVariable Long articleId) {

        VoteResult result = articleVoteService.voteArticle(articleId, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Map<String, Long>> createArticle(@AuthenticationPrincipal CustomUserDetails user,
                                                           @RequestBody @Valid ArticleDTO articleDTO) {

        articleService.createArticle(user.getId(), articleDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", articleDTO.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{articleId}")
    public ResponseEntity<Map<String, Long>> updateArticle(@AuthenticationPrincipal CustomUserDetails user,
                                                           @PathVariable Long articleId,
                                                           @RequestBody @Valid ArticleDTO articleDTO) {

        articleService.updateArticle(articleId, user.getId(), articleDTO);

        return ResponseEntity.ok(Map.of("id", articleDTO.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{articleId}")
    public String deleteArticle(@AuthenticationPrincipal CustomUserDetails user,
                                @PathVariable Long articleId,
                                RedirectAttributes redirectAttributes) {

        articleService.deleteArticle(articleId, user.getId());

        redirectAttributes.addFlashAttribute("toast",
                ToastDTO.createToast("게시물을 삭제했습니다."));

        return "redirect:/article";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/form")
    public String articleForm(@AuthenticationPrincipal CustomUserDetails user,
                              @RequestParam(required = false) Long articleId,
                              Model model) {

        ArticleDTO articleDTO;

        if (articleId != null) {

            articleDTO = articleService.getArticleByOwner(articleId, user.getId());
        } else {

            articleDTO = new ArticleDTO();
        }

        System.out.println("TEST^");

        List<ArticleCategoryDTO> articleCategoryDTOs = articleCategoryService.getCategories();

        System.out.println("TEST#");

        model.addAttribute("articleDTO", articleDTO);
        model.addAttribute("categoryDTOs", articleCategoryDTOs);

        return "article_form";
    }

    @GetMapping("/{articleId}")
    public String view(@PathVariable Long articleId,
                       Model model) {

        model.addAttribute("articleDTO", articleService.getArticleDetail(articleId));
        model.addAttribute("commentDTOs", commentService.getComments(articleId));
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
