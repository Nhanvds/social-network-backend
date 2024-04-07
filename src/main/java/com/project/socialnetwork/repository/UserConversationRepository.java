package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.UserConversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserConversationRepository extends JpaRepository<UserConversation,Long> {
}
