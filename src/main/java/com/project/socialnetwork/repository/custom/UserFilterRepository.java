package com.project.socialnetwork.repository.custom;

import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.entity.User;
import com.project.socialnetwork.mapper.Mapper;
import com.project.socialnetwork.response.UserCard;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


import java.lang.reflect.Field;
import java.util.List;

import static com.project.socialnetwork.database.Tables.*;

@Repository
@RequiredArgsConstructor
public class UserFilterRepository {
    private final DSLContext dslContext;


    public PageImpl<UserCard> searchUser(PageFilterDto<UserFilerDto> input, Pageable pageable, Long myId) {

        SelectQuery userRecords = dslContext.select()
                .from(USERS)
                .leftJoin(USER_ROLE).on(USERS.ID.eq(USER_ROLE.USER_ID))
                .leftJoin(ROLES).on(USER_ROLE.ROLE_ID.eq(ROLES.ID))
                .getQuery();


        Condition condition = DSL.noCondition();


        if (!StringUtils.isEmpty(input.getCommon())) {
            String common = input.getCommon();
            condition = condition.and(
                    USERS.ID.like(common)
                            .or(USERS.USER_NAME.containsIgnoreCase(common))
                            .or(USERS.EMAIL.containsIgnoreCase(common))
                            .or(USERS.DESCRIPTION.containsIgnoreCase(common))

            );
        }

        UserFilerDto userFiler = input.getFilter();
        if (userFiler != null) {
            if (userFiler.getIsFriend()==true) {
                // Tìm danh sách bạn bè
                if(userFiler.getHasAccepted()==true){
                    condition = condition.and((
                            USERS.ID.in(dslContext.select(USER_FRIENDS.FIRST_USER_ID)
                                    .from(USER_FRIENDS)
                                    .where(USER_FRIENDS.SECOND_USER_ID.eq(myId)
                                            .and(USER_FRIENDS.HAS_ACCEPTED.isTrue()))
                                    .groupBy(USER_FRIENDS.FIRST_USER_ID))
                    ).or(

                            USERS.ID.in(dslContext.select(USER_FRIENDS.SECOND_USER_ID)
                                    .from(USER_FRIENDS)
                                    .where(USER_FRIENDS.FIRST_USER_ID.eq(myId)
                                            .and(USER_FRIENDS.HAS_ACCEPTED.isTrue()))
                                    .groupBy(USER_FRIENDS.SECOND_USER_ID))

                    ));
                }else{
                    // Danh sách lời mời kết bạn
                    condition = condition.and((
                            USERS.ID.in(dslContext.select(USER_FRIENDS.FIRST_USER_ID)
                                    .from(USER_FRIENDS)
                                    .where(USER_FRIENDS.SECOND_USER_ID.eq(myId)
                                            .and(USER_FRIENDS.HAS_ACCEPTED.isFalse()))
                                    .groupBy(USER_FRIENDS.FIRST_USER_ID))
                    ).or(

                            USERS.ID.in(dslContext.select(USER_FRIENDS.SECOND_USER_ID)
                                    .from(USER_FRIENDS)
                                    .where(USER_FRIENDS.FIRST_USER_ID.eq(myId)
                                            .and(USER_FRIENDS.HAS_ACCEPTED.isFalse()))
                                    .groupBy(USER_FRIENDS.SECOND_USER_ID))

                    ));
                }




            }else {
                    //Tất cả mọi người
//                    condition = condition.and((
//                            USERS.ID.in(dslContext.select(USER_FRIENDS.FIRST_USER_ID)
//                                    .from(USER_FRIENDS)
//                                    .where(USER_FRIENDS.SECOND_USER_ID.eq(myId))
//                                    .groupBy(USER_FRIENDS.FIRST_USER_ID))
//                    ).or(
//
//                            USERS.ID.in(dslContext.select(USER_FRIENDS.SECOND_USER_ID)
//                                    .from(USER_FRIENDS)
//                                    .where(USER_FRIENDS.FIRST_USER_ID.eq(myId))
//                                    .groupBy(USER_FRIENDS.SECOND_USER_ID))
//
//                    ));
            }

            if (userFiler.getIsLocked() != null) {
                condition = condition.and(USERS.IS_LOCKED.eq(userFiler.getIsLocked()));
            }

            if (userFiler.getId() != null) {
                condition = condition.and(USERS.ID.eq(userFiler.getId()));
            }
            if (!StringUtils.isEmpty(userFiler.getEmail())) {
                condition = condition.and(USERS.EMAIL.eq(userFiler.getEmail()));
            }
            if (!StringUtils.isEmpty(userFiler.getUsername())) {
                condition = condition.and((USERS.USER_NAME.eq(userFiler.getUsername())));
            }
            if (!StringUtils.isEmpty(userFiler.getDescription())) {
                condition = condition.and(USERS.DESCRIPTION.eq(userFiler.getDescription()));
            }

            if (userFiler.getCreatedFrom() != null) {
                condition = condition.and(USERS.CREATED_AT.ge(userFiler.getCreatedFrom()));
            }
            if (userFiler.getCreatedTo() != null) {
                condition = condition.and(USERS.CREATED_AT.le(userFiler.getCreatedTo()));
            }
            if (!StringUtils.isEmpty(userFiler.getRole())) {
                condition = condition.and(ROLES.ROLE_NAME.eq(userFiler.getRole()));
            }
        }

        userRecords.addConditions(condition);

        if (!StringUtils.isEmpty(input.getSortProperty())) {
            String sortProperty = input.getSortProperty();
            for (Field field : User.class.getDeclaredFields()) {
                if (field.getName().equals(sortProperty)) {
                    OrderField<?> orderField = input.getAsc()
                            ? DSL.field(input.getSortProperty()).asc()
                            : DSL.field(input.getSortProperty()).desc();
                    userRecords.addOrderBy(orderField);
                    break;
                }
            }
        }

        userRecords.addGroupBy(USERS.ID);
        long totalCount = userRecords.stream().count();
        if (pageable.isPaged()) {
            userRecords.addOffset(pageable.getPageNumber());
            userRecords.addLimit(pageable.getPageSize());
        }
        List<User> users = userRecords.fetchInto(User.class);

        List<UserCard> userCards = users.stream().map(u -> Mapper.mapToUserCard(u)).toList();
        System.out.println(userRecords.toString());
        return new PageImpl<>(userCards, pageable, totalCount);

    }

}
