package com.project.socialnetwork.service;

import com.project.socialnetwork.configuration.CloudinaryConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService{
    private final CloudinaryConfig cloudinaryConfig;


    @Override
    public String uploadImage(MultipartFile file) {
            try {
                if((file.isEmpty())||file.getSize()>=10*1024*1024){
                    throw new RuntimeException("File ảnh phải nhỏ hơn 10MB!");
                }
                Map data = cloudinaryConfig.getCloudinary().uploader().upload(
                        file.getBytes(), Map.of());
                return (data.get("secure_url").toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
