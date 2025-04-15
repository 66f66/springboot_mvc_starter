package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserRoleConstants;
import f66.springboot_mvc_starter.exception.UniqueConstraintException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.exception.WrongUsernameOrPasswordException;
import f66.springboot_mvc_starter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void createLocalUser(UserDTO userDTO) {

        if (!userDTO.getPassword().equals(userDTO.getPasswordCheck())) {

            throw new UserBadInputException("비밀번호 확인이 일치하지 않습니다.");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userDTO.setRole(UserRoleConstants.ROLE_USER);

        boolean exists = userRepository.existsByUsername(userDTO.getUsername());

        if (exists) {

            throw new UniqueConstraintException("이미 사용중인 아이디입니다.");
        }

        try {

            userRepository.insertLocalUser(userDTO);
        } catch (DuplicateKeyException e) {

            String message = e.getMessage().toLowerCase();

            if (message.contains("username")) {

                throw new UniqueConstraintException("이미 사용중인 아이디입니다.");
            }
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public void signInPreProcess(UserDTO userDTO) {

        UserDTO foundUser = userRepository.selectByUsername(userDTO.getUsername())
                .orElseThrow(WrongUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(userDTO.getPassword(), foundUser.getPassword())) {

            throw new WrongUsernameOrPasswordException();
        }
    }
}
