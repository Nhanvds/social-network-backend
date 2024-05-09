package com.project.socialnetwork.service;

import com.project.socialnetwork.dto.NotificationDto;
import com.project.socialnetwork.exception.ParserTokenException;
import com.project.socialnetwork.response.NotificationResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface PostNotificationService {

    void createNotification(NotificationDto notificationDto);
    void updateReadNotification(Long id);

    PageImpl<NotificationResponse> getNotifications(String token, Pageable pageable) throws ParserTokenException;
}
