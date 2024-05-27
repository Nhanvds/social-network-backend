package com.project.socialnetwork.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email",nullable = false,unique = true,length = 30)
    private String email;

    @Column(name = "user_name",nullable = false,length = 50)
    private String userName;

    @Column(name="url_avatar")
    private String urlAvatar;

    @Column(name = "password" ,nullable = false,length = 100)
    private String password;

    @Column(name = "description",length = 200)
    private String description;

    @Column(name = "is_locked",nullable = false)
    private Boolean isLocked;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;




}
