package com.project.socialnetwork.response;

import com.project.socialnetwork.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private Long id;

    private String email;

    private String username;

    private String urlAvatar;

    private String description;

    private Boolean isLocked;

    private LocalDate lastLogin;

    private Set<Role> roles;
}
