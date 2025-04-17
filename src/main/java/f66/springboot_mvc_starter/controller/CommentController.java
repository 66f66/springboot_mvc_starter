package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.CommentDTO;
import f66.springboot_mvc_starter.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @RequestBody @Valid CommentDTO commentDTO) {

        commentService.createComment(user.getId(), commentDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable Long commentId,
                                              @RequestBody @Valid CommentDTO commentDTO) {

        commentService.updateComment(commentId, user.getId(), commentDTO);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable Long commentId) {

        commentService.deleteComment(commentId, user.getId());

        return ResponseEntity.ok().build();
    }
}
