package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.VerificationToken;
import com.project.socialnetwork.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
    private final VerificationTokenRepository verificationTokenRepository;
    @Value("${verification.token.expiration}")
    private int expiration;

    @Transactional
    public void deleteAllTokensByUserId(Long userId) {
        verificationTokenRepository.deleteByUserId(userId);
    }

    @Override
    public VerificationToken getTokenByUserId(Long userId) {
        return verificationTokenRepository.getVerificationTokenByUserId(userId);
    }

    @Override
    public VerificationToken createVerificationToken(Long userId) {
        UUID uuid = UUID.randomUUID();
        return verificationTokenRepository.save(VerificationToken.builder()
                        .token(uuid.toString())
                        .userId(userId)
                        .expiredAt(LocalDateTime.now().plusHours(expiration))
                .build());
    }
}
