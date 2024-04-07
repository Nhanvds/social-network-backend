package com.project.socialnetwork.repository;

import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.entity.UserFriend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend,Long> {

    @Query("""
        select uf.secondUser from UserFriend uf
        where uf.firstUser.id =:id and uf.hasAccepted=:hasAccepted 
        and (:keyword is null or uf.secondUser.userName like concat('%',:keyword,'%') )
""")
    Page<Long> getUserFriendIds(@Param("id")Long id,@Param("keyword") String keyword,@Param("hasAccepted") Boolean hasAccepted, Pageable pageable);

    @Query("""
    select uf.secondUser from UserFriend uf
    where uf.firstUser.id=:userId and uf.hasAccepted=true
""")
    List<Long> getAllUserFriendIds(@Param("userId") Long userId);

    @Query("""
    select uf from UserFriend uf join fetch uf.firstUser join fetch uf.secondUser
    where uf.secondUser.id in :ids
""")
    List<UserFriend> getUserFriends(@Param("ids") List<Long> ids);

    Optional<UserFriend> getUserFriendById(Long id);


}
