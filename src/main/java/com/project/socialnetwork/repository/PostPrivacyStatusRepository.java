package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostPrivacyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostPrivacyStatusRepository extends JpaRepository<PostPrivacyStatus,Long> {

    Optional<PostPrivacyStatus> getPostPrivacyStatusById(Long id);

    Optional<PostPrivacyStatus> getPostPrivacyStatusByName(String name);

    @Query("select pp from PostPrivacyStatus pp ")
    List<PostPrivacyStatus> getAllPostPrivacyStatus();
}
