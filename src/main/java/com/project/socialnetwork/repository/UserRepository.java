package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);

    @Query("""
    select u from User u
    where u.id=:id
""")
    Optional<User> getUserById(@Param("id") Long id);

    @Query("select u from User u where u.email=:Email")
    Optional<User> getUserByEmail(@Param("Email") String Email);


}
