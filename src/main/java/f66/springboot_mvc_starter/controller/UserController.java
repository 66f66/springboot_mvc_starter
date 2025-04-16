package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import f66.springboot_mvc_starter.dto.ApiResponse;
import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserImageDTO;
import f66.springboot_mvc_starter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ApiResponse updateUser(@AuthenticationPrincipal CustomUserDetails user,
                                  @RequestBody @Valid UserDTO userDTO) {

        userDTO.setId(user.getId());

        userService.updateUser(userDTO);

        return ApiResponse.statusOk();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/image")
    public ApiResponse updateImage(@AuthenticationPrincipal CustomUserDetails user,
                                   @ModelAttribute UserImageDTO userImageDTO) {

        try {

            userService.updateUserProfileImage(user.getId(), userImageDTO);
        } catch (IOException e) {

            return ApiResponse.statusInternalServerError();
        }

        return ApiResponse.statusOk();
    }
}
