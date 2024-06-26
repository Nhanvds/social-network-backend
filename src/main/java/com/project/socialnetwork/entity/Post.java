package com.project.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    @CreationTimestamp
    @Column(name = "created_time",nullable = false)
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(name = "updated_time",nullable = false)
    private LocalDateTime updatedTime;

    @Column(name = "is_locked",nullable = false)
    private Boolean isLocked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY)
    private Set<PostImage> postImages;

    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<PostReaction> postReactions;


}
