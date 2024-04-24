package com.project.socialnetwork.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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
    private LocalDate lastLogin;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
        name = "user_friends",
        joinColumns = @JoinColumn(name = "first_user_id"),
        inverseJoinColumns = @JoinColumn(name = "second_user_id")
    )
    private Set<User> userFriends;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<PostNotification> postNotifications;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "conversations",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_conversation_id")
    )
    private Set<UserConversation> userConversations;


}
