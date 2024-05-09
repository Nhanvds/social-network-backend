package com.project.socialnetwork.dto.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostFilterDto {

    private long id;

    private String content;

    private Long userId;

    private Boolean isLocked;

    private LocalDateTime createdTimeFrom;

    private LocalDateTime createdTimeTo;

    private Boolean hasSeen;

    private Boolean hasLiked;

}
