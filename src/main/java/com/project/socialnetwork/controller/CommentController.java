package com.project.socialnetwork.controller;

import com.project.socialnetwork.dto.PostCommentDTO;
import com.project.socialnetwork.response.ListPostCommentResponse;
import com.project.socialnetwork.response.MessageResponse;
import com.project.socialnetwork.response.PostCommentResponse;
import com.project.socialnetwork.service.PostCommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/comments")
public class CommentController {
    private final PostCommentServiceImpl postCommentService;


    @PostMapping("")
    public ResponseEntity<?> createPostComment(
            @RequestBody PostCommentDTO postCommentDTO,
            @RequestHeader("Authorization") String token
            ){
        token= token.substring(7);
        PostCommentResponse postCommentResponse = postCommentService.createPostComment(
                postCommentDTO,token
        );
        return ResponseEntity.ok().body(postCommentResponse);
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deletePostComment(
            @PathVariable("comment_id") Long id,
            @RequestHeader("Authorization") String token
    ){
        token= token.substring(7);
        postCommentService.deletePostComment(id,token);
        return ResponseEntity.ok().body(MessageResponse.builder()
                .message("Xóa comment thành công!"));
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostComments(
            @PathVariable("post_id")Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("createdAt").descending());
        ListPostCommentResponse listPostCommentResponse = postCommentService.getPostComments(postId,pageRequest);
        return ResponseEntity.ok().body(listPostCommentResponse);
    }
}
