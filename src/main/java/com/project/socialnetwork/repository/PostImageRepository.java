package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {

    @Query("""
        select pi
        from PostImage pi
        where pi.post.id = :postId
        """)
    List<PostImage> getAllPostImageByPostId(@Param("postId") Long id);
}
