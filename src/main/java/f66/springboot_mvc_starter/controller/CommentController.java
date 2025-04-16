package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.CommentDTO;
import f66.springboot_mvc_starter.exception.ForbiddenException;
import f66.springboot_mvc_starter.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @RequestBody @Valid CommentDTO commentDTO) {

        commentService.createComment(user.getId(), commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/update")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable Long id,
                                              @RequestBody @Valid CommentDTO commentDTO) {

        commentService.getCommentByIdAndUserId(id, user.getId())
                .orElseThrow(ForbiddenException::new);

        commentService.updateComment(commentDTO);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable Long id) {

        CommentDTO oldCommentDTO = commentService.getCommentByIdAndUserId(id, user.getId())
                .orElseThrow(ForbiddenException::new);

        commentService.deleteComment(id, oldCommentDTO.getArticleId());

        return ResponseEntity.ok().build();
    }
}
