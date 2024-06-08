package com.project.socialnetwork.controller;

import com.project.socialnetwork.dto.PostCommentDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ApiResponse;
import com.project.socialnetwork.response.PostCommentResponse;
import com.project.socialnetwork.service.impl.PostCommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
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
            @RequestBody PostCommentDto postCommentDTO,
            @RequestHeader("Authorization") String token
    ) throws ParserTokenException {
        PostCommentResponse postCommentResponse = postCommentService.createPostComment(
                postCommentDTO, token
        );
        return ResponseEntity.ok().body(new ApiResponse<PostCommentResponse>("success", postCommentResponse));
    }

    @DeleteMapping("/{comment_id}")
    public ResponseEntity<?> deletePostComment(
            @PathVariable("comment_id") Long id,
            @RequestHeader("Authorization") String token
    ) throws ParserTokenException {
        postCommentService.deletePostComment(id, token);
        return ResponseEntity.ok().body(new ApiResponse("success"));
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostComments(
            @PathVariable("post_id") Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(name = "asc", defaultValue = "false") boolean asc
    ) {
        PageRequest pageRequest;
        if (asc == true) {
            pageRequest = PageRequest.of(page, limit,
                    Sort.by("createdAt").ascending());
        } else {
            pageRequest = PageRequest.of(page, limit,
                    Sort.by("createdAt").descending());
        }

        return ResponseEntity.ok().body(
                new ApiResponse<PageImpl<PostCommentResponse>>("success", postCommentService.getPostComments(postId, pageRequest))
        );
    }
}
