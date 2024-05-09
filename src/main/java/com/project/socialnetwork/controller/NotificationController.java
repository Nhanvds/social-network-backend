package com.project.socialnetwork.controller;

import com.project.socialnetwork.dto.NotificationDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.ApiResponse;
import com.project.socialnetwork.response.NotificationResponse;
import com.project.socialnetwork.service.PostNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final PostNotificationService postNotificationService;

    @PostMapping("")
    public ResponseEntity<?> createNotification(
            @RequestBody NotificationDto notificationDto
            ){
        postNotificationService.createNotification(notificationDto);
        return ResponseEntity.ok()
                .body(new ApiResponse("success"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReadNotification(
            @PathVariable("id") Long id
    ){
        postNotificationService.updateReadNotification(id);
        return ResponseEntity.ok()
                .body(new ApiResponse("success"));
    }

    @GetMapping("")
    public ResponseEntity<?> getListNotifications(
        @RequestHeader("Authorization") String token,
        @RequestParam(name = "page",defaultValue = "0") int page,
        @RequestParam(name = "limit",defaultValue = "10") int limit
    ) throws ParserTokenException {
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("sendedAt").descending());

        return ResponseEntity.ok()
                .body(new ApiResponse<PageImpl<NotificationResponse>>("success",postNotificationService.getNotifications(token,pageRequest)));

    }

}
