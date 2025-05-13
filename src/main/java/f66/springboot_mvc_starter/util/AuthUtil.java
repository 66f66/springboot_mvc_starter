package f66.springboot_mvc_starter.util;

import f66.springboot_mvc_starter.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtil {

    /**
     * @return 인증 된 사용자라면 true 반환
     */
    public boolean isAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * @return 인증 된 사용자라면 id 값 반환, 인증 된 사용자가 아니라면 null 값 반환
     */
    public Optional<Long> getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            return Optional.of(userDetails.getId());
        }

        return Optional.empty();
    }

    public void updateContextUser(String newNickname,
                                  String newImageUrl) {

        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (newNickname != null) userDetails.setNickname(newNickname);
        if (newImageUrl != null) userDetails.setImageUrl(newImageUrl);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }
}
