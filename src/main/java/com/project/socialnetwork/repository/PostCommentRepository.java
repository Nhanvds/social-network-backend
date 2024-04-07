package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment,Long> {


    @Query("""
        select pc.id
        from PostComment pc 
        where pc.post.id = :postId 
    """)
    Page<Long> getPostCommentIdsByPostId(@Param("postId") Long id, Pageable pageable);

    @Query("""
    select pc
    from PostComment pc join fetch pc.user
    where pc.id in :ids
""")
    List<PostComment> getPagePostComment(@Param("ids") List<Long> ids);

    @Query("""
select pc from PostComment pc join fetch pc.user
where pc.id=:id
""")
    Optional<PostComment> getPostCommentById(Long id);
}
