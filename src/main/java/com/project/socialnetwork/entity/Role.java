package com.project.socialnetwork.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Roles")
public class Role {
    public static String ADMIN = "ADMIN";
    public static String USER = "USER";
    public static String UNVERIFIED = "UNVERIFIED";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name",nullable = false,unique = true,length = 20)
    private String roleName;
}
