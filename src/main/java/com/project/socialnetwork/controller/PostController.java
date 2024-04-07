package com.project.socialnetwork.controller;

import com.project.socialnetwork.dto.PostDTO;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.MessageResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import com.project.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/posts")
public class PostController {
    private final PostService postService;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody()PostDTO postDTO,
                                        @RequestHeader("Authorization")String token)
    {
        token = token.substring(7);
        PostDetailResponse postDetailResponse = postService.createPost(postDTO,token);
        return ResponseEntity.ok().body(postDetailResponse);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostById(@PathVariable("post_id")Long postId){
        PostDetailResponse postDetailResponse= postService.getPostDetailResponse(postId);
        return ResponseEntity.ok().body(postDetailResponse);
    }

    @GetMapping("/news-feed")
    public ResponseEntity<?> getNewsFeed(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "30") int limit

    ){
        token = token.substring(7);
        PageRequest pageRequest =PageRequest.of(page,limit,
                Sort.by("createdTime").descending());
        ListPostResponse listPostResponse =postService.getListPosts(token,pageRequest);
        return ResponseEntity.ok().body(listPostResponse);

    }

    @GetMapping("/my-posts")
    public ResponseEntity<?> getMyListPosts(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "30") int limit
    ){
        token = token.substring(7);
        PageRequest pageRequest =PageRequest.of(page,limit,
                Sort.by("createdTime").descending());
        ListPostResponse listPostResponse =postService.getMyListPosts(token,pageRequest);
        return ResponseEntity.ok().body(listPostResponse);
    }

    @GetMapping("/liked-posts")
    public ResponseEntity<?> getLikedPosts(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "30") int limit
    ){
        token = token.substring(7);
        PageRequest pageRequest =PageRequest.of(page,limit,
                Sort.by("createdTime").descending());
        ListPostResponse listPostResponse =postService.getListPostsLiked(token,pageRequest);
        return ResponseEntity.ok().body(listPostResponse);
    }




    @DeleteMapping("/{portId}")
    public ResponseEntity<?> deletePost(
            @PathVariable("portId")Long id,
            @RequestHeader("Authorization") String token
    ){
        postService.deletePost(id,token);
        return ResponseEntity.ok().body(MessageResponse.builder().message("Xóa post thành công!"));
    }

}
