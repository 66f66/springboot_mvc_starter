package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserImageDTO;
import f66.springboot_mvc_starter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/settings")
    public String userSettings(@AuthenticationPrincipal CustomUserDetails user,
                               Model model) {

        UserDTO userDTO = userService.selectUserById(user.getId());

        model.addAttribute("userDTO", userDTO);

        return "user_settings";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update")
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestBody @Valid UserDTO userDTO) {

        userDTO.setId(user.getId());

        userService.updateUser(userDTO);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/image")
    public ResponseEntity<Map<String, String>> updateImage(@AuthenticationPrincipal CustomUserDetails user,
                                                           @ModelAttribute UserImageDTO userImageDTO,
                                                           Authentication authentication) {

        UserDTO userDTO;
        try {

            userDTO = userService.updateUserProfileImage(user.getId(), userImageDTO);
        } catch (IOException e) {

            return ResponseEntity.internalServerError().build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userDetails.setImageUrl(userDTO.getImage().getUrl());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);

        return ResponseEntity.ok().body(Map.of("imageUrl", userDTO.getImage().getUrl()));
    }
}
