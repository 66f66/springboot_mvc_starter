package f66.springboot_mvc_starter.config;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    private transient Boolean admin;

    public CustomUserDetails(Long id,
                             String username,
                             String password,
                             Collection<? extends GrantedAuthority> authorities
    ) {

        this.id = id;
        this.username = username;
        this.password = password;
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

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
