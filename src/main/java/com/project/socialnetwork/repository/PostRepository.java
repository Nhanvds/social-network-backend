package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {



    @Query("""
    select p from Post p join fetch p.user join fetch p.postImages join fetch p.postReactions join p.postPrivacyStatus
    where p.id in :ids
""")
    List<Post> getPostsByIds(@Param("ids")List<Long> ids);

    @Query("""
        select p.id from Post p 
        where p.user.id=:id
""")
    Page<Long> getMyPostIds(@Param("id") Long id,Pageable pageable);

//    @Query("""
//        select  p.id from Post p
//        where p.user.id=:id and p.postPrivacyStatus.name="public"
//""")
//    Page<Long> getPostPublicIds(@Param("id") Long id,Pageable pageable);


    @Query("""
    select p from Post p left join fetch p.postImages left join fetch p.postReactions
    where p.id=:id
""")
    Optional<Post> getPostById(@Param("id") Long id);
}
