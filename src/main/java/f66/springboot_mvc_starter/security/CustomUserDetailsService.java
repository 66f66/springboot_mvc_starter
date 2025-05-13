package f66.springboot_mvc_starter.security;

import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDTO userDTO = userRepository.selectUserWithRelationsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));

        Set<GrantedAuthority> authorities = userDTO.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRoleType().name()))
                .collect(Collectors.toSet());

        return new CustomUserDetails(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getNickname(),
                userDTO.getPassword(),
                userDTO.getImageUrl(),
                authorities
        );
    }
}
