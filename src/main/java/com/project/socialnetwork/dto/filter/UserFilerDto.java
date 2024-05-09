package com.project.socialnetwork.dto.filter;

import com.project.socialnetwork.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class UserFilerDto {

    private Long id;
    private String email;
    private String username;
    private String description;
    private Boolean isFriend=false;
    private Boolean hasAccepted;
    private Boolean isLocked=false;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private String role;

}
