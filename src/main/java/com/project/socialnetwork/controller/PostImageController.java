package com.project.socialnetwork.controller;

import com.project.socialnetwork.response.ApiResponse;
import com.project.socialnetwork.service.PostImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RequestMapping("${api.prefix}/post-images")
@RestController
@RequiredArgsConstructor
public class PostImageController {
    private final PostImageService postImageService;

    @PostMapping("")
    public ResponseEntity<?> uploadImageToCloud(
            @ModelAttribute("files") List<MultipartFile> files
            ){
        List<String> urls= new ArrayList<>(this.postImageService.uploadImage(files));
        return ResponseEntity.ok().body(new ApiResponse<List<String>>("success",urls));
    }


}
