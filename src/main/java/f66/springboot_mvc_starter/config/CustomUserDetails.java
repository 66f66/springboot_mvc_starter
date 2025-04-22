package f66.springboot_mvc_starter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    @Setter
    private String nickname;

    @Setter
    private String imageUrl;

    public CustomUserDetails(Long id,
                             String username,
                             String nickname,
                             String password,
                             String imageUrl,
                             Collection<? extends GrantedAuthority> authorities
    ) {

        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.imageUrl = imageUrl;
        this.authorities = authorities;
    }

    public boolean hasAuthority(String authority) {

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(auth -> auth.equals(authority));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
