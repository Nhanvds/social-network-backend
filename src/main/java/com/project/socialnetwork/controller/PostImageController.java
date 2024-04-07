package com.project.socialnetwork.controller;

import com.project.socialnetwork.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RequestMapping("${api.prefix}/post-images")
@RestController
@RequiredArgsConstructor
public class PostImageController {
    private final PostImageService postImageService;

    @PostMapping("")
    public ResponseEntity<?> uploadImageToCloud(
            @ModelAttribute("file") MultipartFile file
            ){

        String urlImage = postImageService.uploadImage(file);
        return ResponseEntity.ok().body(urlImage);
    }


}
