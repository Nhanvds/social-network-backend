package com.project.socialnetwork.service.impl;

import com.project.socialnetwork.components.JwtUtils;
import com.project.socialnetwork.dto.NotificationDto;
import com.project.socialnetwork.entity.Post;
import com.project.socialnetwork.entity.PostNotification;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.repository.PostNotificationRepository;
import com.project.socialnetwork.repository.PostRepository;
import com.project.socialnetwork.repository.UserRepository;
import com.project.socialnetwork.response.NotificationResponse;
import com.project.socialnetwork.service.PostNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostNotificationServiceImpl implements PostNotificationService {
    private final PostNotificationRepository postNotificationRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtils jwtUtils;
    @Override
    public void createNotification(NotificationDto notificationDto) {
        User user = userRepository.getUserById(notificationDto.getReceiverId())
                .orElseThrow(()-> new RuntimeException("User not existed"));
        Post post = postRepository.getPostById(notificationDto.getPostId())
                .orElseThrow(()->new RuntimeException("Post not existed"));
        PostNotification notification = PostNotification.builder()
                .content(notificationDto.getContent())
                .user(user)
                .post(post)
                .hasRead(false)
                .build();
        this.postNotificationRepository.save(notification);
    }

    @Override
    public void updateReadNotification(Long id) {
        PostNotification postNotification = postNotificationRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Thông báo không tồn tại"));
        postNotification.setHasRead(true);
        postNotificationRepository.save(postNotification);
    }

    @Override
    public PageImpl<NotificationResponse> getNotifications(String token, Pageable pageable) throws ParserTokenException {
        Long userId = jwtUtils.getUserId(token);
        Page<Long> ids = postNotificationRepository.getPostNotificationsIdsByUserId(userId,pageable);
        Long total= ids.getTotalElements();
        List<PostNotification> postNotifications = postNotificationRepository
                .getPagePostNotifications(ids.getContent());
        List<NotificationResponse> notificationResponses = postNotifications.stream()
                .map(notification -> Mapper.mapToNotificationResponse(notification)).toList();

        return new PageImpl<NotificationResponse>(notificationResponses,pageable,total);

    }
}
