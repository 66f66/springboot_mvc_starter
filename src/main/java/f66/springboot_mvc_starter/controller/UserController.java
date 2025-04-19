package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserImageDTO;
import f66.springboot_mvc_starter.service.UserService;
import f66.springboot_mvc_starter.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final AuthUtil authUtil;

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
    public ResponseEntity<Map<String, UserDTO>> updateUser(@AuthenticationPrincipal CustomUserDetails user,
                                                           @RequestBody @Valid UserDTO userDTO,
                                                           Authentication authentication) {

        userDTO.setId(user.getId());

        UserDTO updatedUserDTO = userService.updateUser(userDTO);

        authUtil.updateContextUserImageUrl(authentication, updatedUserDTO.getImageUrl());

        return ResponseEntity.ok().body(Map.of("user", updatedUserDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/image")
    public ResponseEntity<Map<String, String>> updateImage(@AuthenticationPrincipal CustomUserDetails user,
                                                           @ModelAttribute UserImageDTO userImageDTO,
                                                           Authentication authentication) {

        String imageUrl;

        try {

            imageUrl = userService.updateUserProfileImage(user.getId(), userImageDTO).getUrl();
        } catch (IOException e) {

            return ResponseEntity.internalServerError().build();
        }

        authUtil.updateContextUserImageUrl(authentication, imageUrl);

        return ResponseEntity.ok().body(Map.of("imageUrl", imageUrl));
    }
}
