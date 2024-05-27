package com.project.socialnetwork.dto.filter;

import com.project.socialnetwork.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserFilerDto {

    private Long id;
    private String email;
    private String userName;
    private Boolean isLocked;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private String role;

}
