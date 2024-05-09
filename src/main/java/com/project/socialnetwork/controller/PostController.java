package com.project.socialnetwork.controller;

import com.project.socialnetwork.dto.PostCommentDto;
import com.project.socialnetwork.dto.PostDto;
import com.project.socialnetwork.dto.PostReactionDto;
import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.PostFilterDto;
import com.project.socialnetwork.entity.PostPrivacyStatus;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ApiResponse;
import com.project.socialnetwork.response.ListPostResponse;
import com.project.socialnetwork.response.PostDetailResponse;
import com.project.socialnetwork.response.PostResponse;
import com.project.socialnetwork.service.PostCommentService;
import com.project.socialnetwork.service.PostPrivacyStatusService;
import com.project.socialnetwork.service.PostReactionService;
import com.project.socialnetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/posts")
public class PostController {
    private final PostService postService;
    private final PostCommentService postCommentService;
    private final PostReactionService postReactionService;
    private final PostPrivacyStatusService postPrivacyStatusService;


    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody() PostDto postDTO,
                                        @RequestHeader("Authorization") String token) throws ParserTokenException {

        PostDetailResponse postDetailResponse = postService.createPost(postDTO, token);
        return ResponseEntity.ok().body(new ApiResponse<PostDetailResponse>("success", postDetailResponse));
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<?> getPostById(@PathVariable("post_id") Long postId) {
        PostDetailResponse postDetailResponse = postService.getPostDetailResponse(postId);
        return ResponseEntity.ok().body(new ApiResponse<PostDetailResponse>("success", postDetailResponse));
    }

    @PostMapping("/list")
    public ResponseEntity<?> searchPost(@RequestBody PageFilterDto<PostFilterDto> input,
                                        @RequestHeader("Authorization") String token) throws ParserTokenException {
        return ResponseEntity.ok()
                .body(new ApiResponse<PageImpl<PostResponse>>("ok", postService.searchPost(input, token)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long id,
                                        @RequestBody PostDto postDto) {
        postService.updatePost(id, postDto);
        return ResponseEntity.ok()
                .body(new ApiResponse("Cập nhật bài viết thành công"));
    }

    @PostMapping("/comment")
    public ResponseEntity<?> commentPost(@RequestBody PostCommentDto postCommentDto,
                                         @RequestHeader("Authorization") String token) throws ParserTokenException {
        postCommentService.createPostComment(postCommentDto, token);
        return ResponseEntity.ok()
                .body(new ApiResponse("success"));
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long reactionId,
                                           @RequestHeader("Authorization") String token) throws ParserTokenException {
        postCommentService.deletePostComment(reactionId, token);
        return ResponseEntity.ok()
                .body(new ApiResponse("success"));

    }

    @PostMapping("/like")
    public ResponseEntity<?> createReaction(@RequestBody PostReactionDto postReactionDto,
                                            @RequestHeader("Authorization") String token) throws ParserTokenException {
        postReactionService.createPostReaction(postReactionDto,token);
        return ResponseEntity.ok()
                .body(new ApiResponse("success"));
    }


    @DeleteMapping("/{portId}")
    public ResponseEntity<?> deletePost(
            @PathVariable("portId") Long id,
            @RequestHeader("Authorization") String token
    ) throws ParserTokenException {
        postService.deletePost(id, token);
        return ResponseEntity.ok().body(new ApiResponse("Xóa post thành công!"));
    }

    @GetMapping("/post-privacy")
    public ResponseEntity<?> getAllPostPrivacy() {
        List<PostPrivacyStatus> list = postPrivacyStatusService.getAllPostPrivacyStatus();
        return ResponseEntity.ok()
                .body(new ApiResponse<List<PostPrivacyStatus>>("success", list));
    }

    @GetMapping("post-privacy/{id}")
    public ResponseEntity<?> getPostPrivacyById(@PathVariable("id") Long id) {
        PostPrivacyStatus postPrivacyStatus = postPrivacyStatusService.getPostPrivacyStatusById(id);
        return ResponseEntity.ok()
                .body(new ApiResponse<PostPrivacyStatus>("success", postPrivacyStatus));
    }




}
