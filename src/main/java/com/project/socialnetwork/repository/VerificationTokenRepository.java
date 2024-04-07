package com.project.socialnetwork.repository;


import com.project.socialnetwork.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {

    void deleteByUserId(Long userId);


    VerificationToken getVerificationTokenByUserId(Long userId);
}
