package com.project.socialnetwork.service;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.components.JwtCreate;
import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.entity.Role;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.UserFriend;
import com.project.socialnetwork.entity.VerificationToken;
import com.project.socialnetwork.enums.ErrorCode;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.RoleRepository;
import com.project.socialnetwork.repository.UserFriendRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.Token;
import com.project.socialnetwork.response.UserCard;
import com.project.socialnetwork.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserFriendRepository userFriendRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtCreate jwtCreate;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Override
    public UserDetailResponse createUser(UserDTO userDTO) throws InvalidCredentialsException {
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
        user.setRoles(roles);
        user.setIsLocked(false);
        User addedUser = userRepository.save(user);
        return Mapper.mapToUserDetailResponse(addedUser);
    }

    @Override
    public Token login(UserDTO userDTO) throws InvalidCredentialsException, JOSEException {
        User user = userRepository.getUserByEmail(userDTO.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.EMAIL_PASSWORD_WRONG));
        String password = userDTO.getPassword();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(ErrorCode.EMAIL_PASSWORD_WRONG);
        }
        String token = jwtCreate.generateToken(user);
        return Token.builder()
                .token(token).build();
    }

    @Override
    public boolean verifyUser(String verificationToken, Long userId) throws InvalidCredentialsException{
        User user = userRepository.getUserById(userId)
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED));
//        String message = new String("Xác thực tài khoản thất bại!");
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
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED) );
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
    public List<UserCard> findUser(String keyword, Pageable pageable) {
        Page<Long> ids = userRepository.searchUserIds(keyword,pageable);
        List<User> userList = userRepository.getUsersByIds(ids.toList());
        List<UserCard> userCardList = userList.stream()
                .map(user -> Mapper.mapToUserCard(user)).toList();
        return userCardList;
    }

    @Override
    public void sendFriendRequest(String token, Long userFriendId) throws ParserTokenException, InvalidCredentialsException {
        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(()-> new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED) );
        User friend = userRepository.getUserById(userFriendId)
                .orElseThrow(()->new InvalidCredentialsException(ErrorCode.USER_NOT_EXISTED) );
        UserFriend userFriend = UserFriend.builder()
                .firstUser(user)
                .secondUser(friend)
                .hasAccepted(false)
                .build();
        userFriendRepository.save(userFriend);
    }

    @Override
    public void acceptFriendRequest(Long friendRequestId) throws InvalidCredentialsException {
        UserFriend userFriend = userFriendRepository.getUserFriendById(friendRequestId)
                .orElseThrow(()->new InvalidCredentialsException(ErrorCode.FRIEND_REQUEST_NOT_EXISTED) );
        userFriend.setHasAccepted(true);
        userFriendRepository.save(userFriend);
    }
}
