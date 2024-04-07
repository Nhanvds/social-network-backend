package com.project.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostImageService {
    String uploadImage(MultipartFile file);
}
