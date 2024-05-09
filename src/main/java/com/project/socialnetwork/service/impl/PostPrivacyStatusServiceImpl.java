package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.entity.PostPrivacyStatus;
import com.project.socialnetwork.repository.PostPrivacyStatusRepository;
import com.project.socialnetwork.service.PostPrivacyStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPrivacyStatusServiceImpl implements PostPrivacyStatusService {
    private final PostPrivacyStatusRepository postPrivacyStatusRepository;

    @Override
    public List<PostPrivacyStatus> getAllPostPrivacyStatus() {
        List<PostPrivacyStatus> postPrivacyStatusList = postPrivacyStatusRepository
                .getAllPostPrivacyStatus();
        return postPrivacyStatusList;
    }

    @Override
    public PostPrivacyStatus getPostPrivacyStatusById(long id) {
        PostPrivacyStatus postPrivacyStatus = postPrivacyStatusRepository.getPostPrivacyStatusById(id)
                .orElseThrow(() -> new RuntimeException("Chế độ bài viết không tồn tại"));
        return postPrivacyStatus;
    }
}
