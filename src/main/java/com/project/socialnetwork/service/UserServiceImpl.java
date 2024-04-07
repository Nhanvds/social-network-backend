package com.project.socialnetwork.service;

import com.nimbusds.jose.JOSEException;
import com.project.socialnetwork.components.JwtCreate;
import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.UserDTO;
import com.project.socialnetwork.entity.Role;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.VerificationToken;
import com.project.socialnetwork.exception.InvalidCredentialsException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.RoleRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final JwtCreate jwtCreate;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;

    @Override
    public User createUser(UserDTO userDTO) {
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("Email này đã được sử dụng!");
        }
        // Mặc định tạo tài khoản với role là là UNVERIFIED
        Role userRole = roleRepository.getRoleByName(Role.UNVERIFIED)
                .orElseThrow(()->new RuntimeException("Không thể tạo User"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        User user = Mapper.mapToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        user.setIsLocked(false);

        return userRepository.save(user);
    }

    @Override
    public Token login(UserDTO userDTO) throws InvalidCredentialsException, JOSEException, ParseException {
        User user = userRepository.getUserByEmail(userDTO.getEmail())
                .orElseThrow(()->new InvalidCredentialsException("Email hoặc mật khẩu không chính xác!"));
        String password = userDTO.getPassword();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new InvalidCredentialsException("Email hoặc mật khẩu không chính xác!!");
        }
        String token = jwtCreate.generateToken(user);
        return Token.builder()
                .token(token)
                .build();
    }

    @Override
    public String verifyUser(String verificationToken,String token) throws ParseException {
        Long userId = jwtUtils.getUserId(token);
        User user = userRepository.getUserById(userId)
                .orElseThrow(()-> new RuntimeException("User không tồn tại!"));
        String message = new String("Xác thực tài khoản thất bại!");
        VerificationToken existingVerificationToken = verificationTokenService.getTokenByUserId(userId);
        if(LocalDateTime.now().isAfter(existingVerificationToken.getExpiredaAt())
        || !existingVerificationToken.getToken().equals(verificationToken)){
            return message;
        }
        // cập nhật tài khoản sang role User
        Role userRole = roleRepository.getRoleByName(Role.USER)
                .orElseThrow(()->new RuntimeException("Không thể tạo User"));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
        message = "Xác thực tài khoản thành công!";
        // Xóa verification token
        verificationTokenService.deleteAllTokensByUserId(userId);
        return message;
    }

    @Override
    public void sendEmail(String email, String token) throws ParseException {
        Long userId = jwtUtils.getUserId(token);
        verificationTokenService.deleteAllTokensByUserId(userId);//Xóa hết mã cũ
        VerificationToken verificationToken = verificationTokenService.createVerificationToken(userId);
        emailService.sendMail(email,"Xác thực tài khoản Social Network",
                verificationToken.getToken());
    }


}
