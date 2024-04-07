package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.PostNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostNotificationRepository extends JpaRepository<PostNotification,Long> {

    @Query("""
    select pn.id
    from PostNotification pn
    where pn.user.id=:userId
""")
    Page<Long> getPostNotificationsIdsByUserId(@Param("userId") Long id, Pageable pageable);

    @Query("""
    select pn
    from PostNotification pn join fetch pn.post
    where pn.id in :ids
""")
    List<PostNotification> getPagePostNotifications(@Param("ids") List<Long> ids);


}
