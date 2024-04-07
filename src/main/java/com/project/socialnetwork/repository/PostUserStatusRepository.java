package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostUserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostUserStatusRepository extends JpaRepository<PostUserStatus,Long> {
    // Lấy danh sách post chưa xem của bạn bè, post công khai
    @Query("""
    select pus.post.id from PostUserStatus pus
    where pus.id=:userId and pus.hasSeen=false and (pus.post.postPrivacyStatus.name <> :privacy )
""")
    Page<Long> getNewsFeedIdsByUserId(@Param("userId")Long id,String privacy, Pageable pageable);

}
