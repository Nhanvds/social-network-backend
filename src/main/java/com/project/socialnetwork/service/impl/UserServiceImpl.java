package com.project.socialnetwork.service.impl;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.components.JwtCreate;
import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.UserDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.entity.Role;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.VerificationToken;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.RoleRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.repository.custom.UserFilterRepository;
import com.project.socialnetwork.response.Token;
import com.project.socialnetwork.response.UserCard;
import com.project.socialnetwork.response.UserDetailResponse;
import com.project.socialnetwork.service.UserService;
import com.project.socialnetwork.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserFilterRepository userFilterRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtCreate jwtCreate;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private String defaultAvatar = "https://res.cloudinary.com/draknr12v/image/upload/v1711188334/wlqmuizwtnfnrnnlqojk.jpg";

    @Override
    public UserDetailResponse createUser(UserDto userDTO) throws InvalidCredentialsException {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new InvalidCredentialsException(ErrorCode.EMAIL_EXISTED);
        }
        // Mặc định tạo tài khoản với role là là UNVERIFIED
        Role userRole = roleRepository.getRoleByName(Role.UNVERIFIED)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = Mapper.mapToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUrlAvatar(defaultAvatar);
        user.setRoles(roles);
        user.setIsLocked(false);
        User addedUser = userRepository.save(user);
        return Mapper.mapToUserDetailResponse(addedUser);
    }

    @Override
    public Token login(UserDto userDTO) throws InvalidCredentialsException, JOSEException {
        User user = userRepository.getUserByEmail(userDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.EMAIL_PASSWORD_WRONG));

        String password = userDTO.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(ErrorCode.EMAIL_PASSWORD_WRONG);
        }
        if(user.getIsLocked()==true){
            throw new RuntimeException("Tài khoản bị khóa");
        }
        String token = jwtCreate.generateToken(user);
        return Token.builder()
                .token(token).build();
    }

    @Override
    public boolean verifyUser(String verificationToken, Long userId) throws InvalidCredentialsException {
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));

        VerificationToken existingVerificationToken = verificationTokenService.getTokenByUserId(userId);
        if (LocalDateTime.now().isAfter(existingVerificationToken.getExpiredAt())
                || !existingVerificationToken.getToken().equals(verificationToken)) {
            return false;
        }
        // cập nhật tài khoản sang role User
        Role userRole = roleRepository.getRoleByName(Role.USER)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.ROLE_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        // Xóa verification token
        verificationTokenService.deleteAllTokensByUserId(userId);
        return true;
    }

    @Override
    public boolean sendEmail(String email) throws InvalidCredentialsException {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        Long userId = user.getId();
        verificationTokenService.deleteAllTokensByUserId(userId);//Xóa hết mã cũ
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(userId);
        emailService.sendMail(email, "Xác thực tài khoản Social Network",
                verificationToken.getToken());
        return true;
    }

    @Override
    public UserDetailResponse getUserDetailByToken(String token) throws InvalidCredentialsException, ParserTokenException {
        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        return Mapper.mapToUserDetailResponse(user);
    }

    @Override
    public UserDetailResponse getUserDetailById(Long userId) throws InvalidCredentialsException {
        User user = userRepository.getUserById(userId)
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        return Mapper.mapToUserDetailResponse(user);
    }

    @Override
    public PageImpl<UserDetailResponse> searchUser(PageFilterDto<UserFilerDto> input,String token) throws ParserTokenException {
        Long userId = jwtUtils.getUserId(token);
        Pageable pageable=input.getPageable();
        return userFilterRepository.searchUser(input, pageable,userId);
    }

    @Override
    public PageImpl<UserCard> getAllUsers(Integer page, Integer limit, String commonSearch, Boolean asc, String sortProperty) {
        Pageable pageable = PageRequest.of(page,limit);
        return userFilterRepository.getAllUser(pageable,commonSearch,asc,sortProperty);
    }

    @Override
    public void updateUserByAdmin(UserDetailResponse user) throws InvalidCredentialsException {
        User existedUser = userRepository.getUserById(user.getId())
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        existedUser.setRoles(user.getRoles().stream().collect(Collectors.toSet()));
        existedUser.setIsLocked(user.getLocked());
        userRepository.save(existedUser);
    }

    @Override
    public void updateUser(Long id, String token, UserDto user) throws InvalidCredentialsException, ParserTokenException {
        User existedUser = userRepository.getUserById(id)
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        Long userId = this.jwtUtils.getUserId(token);
        if(existedUser.getId()!=userId){
            throw new InvalidCredentialsException(ErrorCode.FAIL);
        }
        if(!StringUtils.isEmpty(user.getPassword())){
            existedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        existedUser.setUserName(user.getUsername());
        existedUser.setDescription(user.getDescription());
        if(user.getUrlAvatar()!=null){
            existedUser.setUrlAvatar(user.getUrlAvatar());
        }
        userRepository.save(existedUser);
    }
    @Override
    public void updatePassword(String token, UserDto user) throws InvalidCredentialsException, ParserTokenException {
        User existedUser = userRepository.getUserById(user.getId())
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        String existedToken = verificationTokenService.getTokenByUserId(user.getId()).getToken();
        if(!existedToken.equalsIgnoreCase(token)){
            throw new RuntimeException("Mã xác thực không đúng");
        }
        if(!StringUtils.isEmpty(user.getPassword())){
            existedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(existedUser);
    }

    @Override
    public void checkPassword(Long userId, String password) throws InvalidCredentialsException {
        User existedUser = userRepository.getUserById(userId)
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        if(!passwordEncoder.matches(password.trim(),existedUser.getPassword())){
            throw new RuntimeException("Mật khẩu không khớp");
        }
    }

    @Override
    public UserDetailResponse getUserDetailByEmail(String email) throws InvalidCredentialsException {
        User user = userRepository.getUserByEmail(email)
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
        return Mapper.mapToUserDetailResponse(user);
    }
}
