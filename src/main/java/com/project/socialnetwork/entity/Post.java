package com.project.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content",columnDefinition = "TEXT", length = 2000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_privacy_status_id")
    private PostPrivacyStatus postPrivacyStatus;

    @Column(name = "created_time",nullable = false)
    private LocalDate createdTime;

    @Column(name = "updated_time",nullable = false)
    private LocalDate updatedTime;

    @Column(name = "is_locked",nullable = false)
    private Boolean isLocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private Set<PostImage> postImages;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<PostReaction> postReactions;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<PostUserStatus> postUserStatuses;

}
