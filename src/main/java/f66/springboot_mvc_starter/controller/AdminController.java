package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.service.ArticleService;
import f66.springboot_mvc_starter.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/article/{id}/delete")
    public String deleteArticle(@PathVariable Long id,
                                @RequestParam int categoryId) {

        return "redirect:/article";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam Long articleId,
                                RedirectAttributes redirectAttributes) {

        redirectAttributes.addAttribute("articleId", articleId);

        return "redirect:/article/{articleId}";
    }
}
