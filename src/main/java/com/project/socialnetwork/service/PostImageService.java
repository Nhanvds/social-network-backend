package com.project.socialnetwork.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostImageService {
    List<String> uploadImage(List<MultipartFile> files);
}
