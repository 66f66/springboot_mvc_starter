package f66.springboot_mvc_starter.service;

import f66.springboot_mvc_starter.dto.*;
import f66.springboot_mvc_starter.exception.UniqueConstraintException;
import f66.springboot_mvc_starter.exception.UserBadInputException;
import f66.springboot_mvc_starter.exception.WrongUsernameOrPasswordException;
import f66.springboot_mvc_starter.repository.RoleRepository;
import f66.springboot_mvc_starter.repository.UserImageRepository;
import f66.springboot_mvc_starter.repository.UserRepository;
import f66.springboot_mvc_starter.util.CloudinaryUtil;
import f66.springboot_mvc_starter.util.FileUtil;
import lombok.RequiredArgsConstructor;
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
    private final RoleRepository roleRepository;
    private final FileUtil fileUtil;
    private final CloudinaryUtil cloudinaryUtil;

    @Transactional
    public void createUser(UserDTO userDTO, RoleType roleType) {

        if (!userDTO.getPassword().equals(userDTO.getPasswordCheck()))
            throw new UserBadInputException("비밀번호 확인이 일치하지 않습니다.");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        boolean exists = userRepository.existsByUsername(userDTO.getUsername());

        if (exists) throw new UniqueConstraintException("이미 사용중인 아이디입니다.");

        try {

            userRepository.insertUser(userDTO);
        } catch (DuplicateKeyException e) {

            String message = e.getMessage().toLowerCase();

            if (message.contains("username")) throw new UniqueConstraintException("이미 사용중인 아이디입니다.");
        }

        RoleDTO roleDTO = roleRepository.selectByRoleType(roleType)
                .orElseThrow();

        userRepository.insertUserRole(userDTO.getId(), roleDTO.getId());

        UserImageDTO userImageDTO = UserImageDTO.builder()
                .userId(userDTO.getId())
                .build();

        userImageRepository.insertImage(userImageDTO);
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public void signInPreProcess(UserDTO userDTO) {

        UserDTO foundUser = userRepository
                .selectUserByUsername(userDTO.getUsername())
                .orElseThrow(WrongUsernameOrPasswordException::new);

        if (!passwordEncoder.matches(userDTO.getPassword(), foundUser.getPassword()))
            throw new WrongUsernameOrPasswordException();
    }

    @Transactional(readOnly = true)
    public UserDTO selectUserById(Long id) {

        return userRepository
                .selectUserWithRelationsById(id)
                .orElseThrow();
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {

        UserDTO oldUser = userRepository
                .selectUserWithRelationsById(userDTO.getId())
                .orElseThrow();

        if (oldUser.getNickname().equals(userDTO.getNickname())) userDTO.setNickname(null);

        if (userDTO.getNewPassword() != null && userDTO.getOldPassword() != null) {

            if (!passwordEncoder.matches(userDTO.getOldPassword(), oldUser.getPassword()))
                throw new UserBadInputException("비밀번호 입력을 확인하세요.");

            userDTO.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        userRepository.updateUser(userDTO);

        return userRepository
                .selectUserWithRelationsById(userDTO.getId())
                .orElseThrow();
    }

    @Transactional
    public UserImageDTO updateUserProfileImage(Long userId,
                                               UserImageDTO userImageDTO) throws IOException {

        MultipartFile file = userImageDTO.getFile();

        final String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png"};
        final long MAX_FILE_SIZE = 1024 * 1024; // 1MB

        fileUtil.validateMultipartFile(file, ALLOWED_CONTENT_TYPES, MAX_FILE_SIZE);

        final String FOLDER_NAME = "user_profile";

        CloudinaryUploadResponse result = cloudinaryUtil
                .uploadMultipartFile(file, Map.of("folder", FOLDER_NAME));

        userImageDTO.setPublicId(result.getPublicId());
        userImageDTO.setOriginalFileName(file.getOriginalFilename());
        userImageDTO.setUrl(result.getUrl());

        UserImageDTO oldImageDTO = userImageRepository
                .selectImageByUserId(userId)
                .orElseThrow();

        userImageDTO.setId(oldImageDTO.getId());

        if (oldImageDTO.getPublicId() != null) {

            userImageRepository.updateImage(userImageDTO);

            cloudinaryUtil.deleteFile(oldImageDTO.getPublicId());
        } else {

            userImageRepository.updateImage(userImageDTO);
        }

        return userImageDTO;
    }
}
