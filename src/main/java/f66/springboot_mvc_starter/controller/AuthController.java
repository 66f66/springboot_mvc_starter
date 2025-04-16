package f66.springboot_mvc_starter.controller;

import f66.springboot_mvc_starter.dto.ToastDTO;
import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.ValidationGroups;
import f66.springboot_mvc_starter.exception.UniqueConstraintException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.exception.WrongUsernameOrPasswordException;
import f66.springboot_mvc_starter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/sign-up")
    public String signUp(UserDTO userDTO,
                         Model model) {

        model.addAttribute("userDTO", userDTO);

        return "auth/sign-up";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sign-up")
    public String signUp(@Validated(ValidationGroups.Create.class) UserDTO userDTO,
                         RedirectAttributes redirectAttributes) {

        try {

            userService.createLocalUser(userDTO);
        } catch (UserBadInputException | UniqueConstraintException e) {

            redirectAttributes.addFlashAttribute("error", e.getMessage());

            return "redirect:/auth/sign-up";
        }

        redirectAttributes.addFlashAttribute("toast",
                ToastDTO.createToast("회원가입 되었습니다."));

        return "redirect:/auth/sign-in";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/exists-by-username")
    public ResponseEntity<Map<String, Boolean>> existsByUsername(@RequestParam String username) {

        return ResponseEntity.ok(Map.of("existsByUsername", userService.existsByUsername(username)));
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/sign-in")
    public String signIn(@RequestParam(value = "redirect_url", required = false) String redirectUrl,
                         Model model) {

        model.addAttribute("redirectUrl", redirectUrl);

        return "auth/sign-in";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sign-in/pre-process")
    public ResponseEntity<Void> signInPreProcess(@RequestBody UserDTO userDTO) {

        try {

            userService.signInPreProcess(userDTO);
        } catch (WrongUsernameOrPasswordException e) {

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
