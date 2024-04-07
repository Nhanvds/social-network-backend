package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageRepository extends JpaRepository<UserMessage,Long> {
}
