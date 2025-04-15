package f66.springboot_mvc_starter.util;

import f66.springboot_mvc_starter.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
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
    public Optional<Long> currentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            return Optional.of(userDetails.getId());
        }

        return Optional.empty();
    }
}
