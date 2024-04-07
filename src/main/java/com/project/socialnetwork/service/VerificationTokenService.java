package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.VerificationToken;

public interface VerificationTokenService {
    void deleteAllTokensByUserId(Long userId);

    VerificationToken getTokenByUserId(Long userId);
    VerificationToken createVerificationToken(Long userId);
}
