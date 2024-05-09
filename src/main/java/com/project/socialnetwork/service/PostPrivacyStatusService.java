package com.project.socialnetwork.service;

import com.project.socialnetwork.entity.PostPrivacyStatus;

import java.util.List;

public interface PostPrivacyStatusService {

    List<PostPrivacyStatus> getAllPostPrivacyStatus();

    PostPrivacyStatus getPostPrivacyStatusById(long id);

}
