package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostReactionRepository extends JpaRepository<PostReaction,Long> {

    @Query("""
    select pr.id from PostReaction pr where pr.post.id=:postId and (:hasLiked is NULL or pr.hasLiked=:hasLiked)
""")
    Page<Long> getPostReactionIdsByPostId(@Param("postId") Long id
            ,@Param("hasLiked")Boolean hasLiked
            , Pageable pageable);

    @Query("""
    select pr 
    from PostReaction pr join fetch pr.user
    where pr.id in :ids
""")
    List<PostReaction> getPagePostReaction(@Param("ids")List<Long> ids);



    @Query("""
    select pr.post.id from PostReaction pr 
    where pr.user.id=:userId and pr.hasLiked=true
""")
    Page<Long> getPostIdsLiked(@Param("userId")Long id,Pageable pageable);


    @Override
    Optional<PostReaction> findById(Long id);

    @Query("""
        select pr from PostReaction pr
        where pr.user.id =:userId and pr.post.id = :postId
""")
    Optional<PostReaction> findIdByUserIdAndPostId(@Param("userId") Long userId,@Param("postId") Long postId);

    @Modifying
    @Query("""
    delete from PostReaction  pr where pr.id=:id
""")
    void deleteById(@Param("id") Long id);

}
