package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.CloudinaryUploadResult;
import f66.springboot_mvc_starter.dto.UserDTO;
import f66.springboot_mvc_starter.dto.UserImageDTO;
import f66.springboot_mvc_starter.exception.ResourceNotFoundException;
import f66.springboot_mvc_starter.exception.UniqueConstraintException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.exception.WrongUsernameOrPasswordException;
import f66.springboot_mvc_starter.repository.UserImageRepository;
import f66.springboot_mvc_starter.repository.UserRepository;
import f66.springboot_mvc_starter.util.CloudinaryUtil;
import f66.springboot_mvc_starter.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final FileUtil fileUtil;
    private final CloudinaryUtil cloudinaryUtil;

    @Value("${config.default-image-url}")
    private String defaultImageUrl;

    @Transactional
    public void createLocalUser(UserDTO userDTO) {

        if (!userDTO.getPassword().equals(userDTO.getPasswordCheck())) {

            throw new UserBadInputException("비밀번호 확인이 일치하지 않습니다.");
        }

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

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

        UserImageDTO userImageDTO = UserImageDTO.builder()
                .url(defaultImageUrl + userDTO.getNickname())
                .build();

        userImageRepository.insertUserImage(userImageDTO);
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

    @Transactional(readOnly = true)
    public UserDTO selectUserById(Long id) {

        return userRepository.selectById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {

        UserDTO oldUser = userRepository.selectById(userDTO.getId())
                .orElseThrow(ResourceNotFoundException::new);

        if (oldUser.getNickname().equals(userDTO.getNickname())) {

            userDTO.setUsername(null);
        }

        if (userDTO.getNickname() != null) {

            UserImageDTO userImageDTO = userImageRepository.selectByUserId(userDTO.getId())
                    .orElseThrow(ResourceNotFoundException::new);

            if (userImageDTO.getPublicId() == null) {

                userImageDTO.setUrl(defaultImageUrl + userDTO.getNickname());

                userImageRepository.updateUserImage(userImageDTO);
            }
        }

        if (userDTO.getNewPassword() != null && userDTO.getOldPassword() != null) {

            if (!passwordEncoder.matches(userDTO.getOldPassword(), oldUser.getPassword())) {

                throw new UserBadInputException("비밀번호 입력을 확인하세요.");
            }

            userDTO.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        userRepository.updateUser(userDTO);

        return userRepository.selectById(userDTO.getId())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public UserImageDTO updateUserProfileImage(Long userId,
                                               UserImageDTO userImageDTO) throws IOException {

        MultipartFile file = userImageDTO.getFile();

        final String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png"};
        final long MAX_FILE_SIZE = 1024 * 1024; // 1MB

        fileUtil.validateFile(file, ALLOWED_CONTENT_TYPES, MAX_FILE_SIZE);

        final String FOLDER_NAME = "user_profile";

        CloudinaryUploadResult result = cloudinaryUtil
                .uploadFile(file, Map.of("folder", FOLDER_NAME));

        userImageDTO.setPublicId(result.getPublicId());
        userImageDTO.setUrl(result.getUrl());
        userImageDTO.setOriginalFileName(file.getOriginalFilename());

        UserImageDTO oldImageDTO = userImageRepository.selectByUserId(userId)
                .orElseThrow(ResourceNotFoundException::new);

        if (oldImageDTO.getPublicId() != null) {

            userImageDTO.setId(oldImageDTO.getId());

            userImageRepository.updateUserImage(userImageDTO);

            cloudinaryUtil.deleteFile(oldImageDTO.getPublicId());
        } else {

            userImageDTO.setId(oldImageDTO.getId());
            userImageDTO.setUserId(userId);

            userImageRepository.updateUserImage(userImageDTO);
        }

        return userImageDTO;
    }
}
