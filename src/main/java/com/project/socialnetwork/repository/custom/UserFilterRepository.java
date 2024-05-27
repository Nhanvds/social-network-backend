package com.project.socialnetwork.repository.custom;

import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.UserFilerDto;
import com.project.socialnetwork.entity.Role;
import com.project.socialnetwork.response.UserCard;
import com.project.socialnetwork.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;


import static com.project.socialnetwork.database.Tables.*;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

@Repository
@RequiredArgsConstructor
public class UserFilterRepository {
    private final DSLContext dslContext;


    public PageImpl<UserDetailResponse> searchUser(PageFilterDto<UserFilerDto> input, Pageable pageable, Long myId) {

        SelectQuery selectQuery = dslContext.select(
                        USERS.ID.as("id"),
                        USERS.USER_NAME.as("username"),
                        USERS.URL_AVATAR.as("urlAvatar"),
                        USERS.EMAIL.as("email"),
                        USERS.DESCRIPTION.as("description"),
                        USERS.CREATED_AT.as("createdAt"),
                        USERS.IS_LOCKED.as("isLocked"),
                        multiset(
                                select().from(ROLES)
                                        .leftJoin(USER_ROLE).on(ROLES.ID.eq(USER_ROLE.ROLE_ID))
                                .where(USER_ROLE.USER_ID.eq(USERS.ID))
                        ).as("roles").convertFrom(r -> r.into(Role.class))
                )
                .from(USERS)
                .leftJoin(USER_ROLE).on(USERS.ID.eq(USER_ROLE.USER_ID))
                .leftJoin(ROLES).on(USER_ROLE.ROLE_ID.eq(ROLES.ID))
                .groupBy(USERS.ID)
                .getQuery();
        Condition condition = DSL.noCondition();
        condition = condition.and(ROLES.ROLE_NAME.ne(Role.UNVERIFIED));
        if (!StringUtils.isEmpty(input.getCommon())) {
            String common = input.getCommon();
            condition = condition.and(
                    USERS.ID.like(common)
                            .or(USERS.USER_NAME.containsIgnoreCase(common))
                            .or(USERS.EMAIL.containsIgnoreCase(common))
                            .or(USERS.DESCRIPTION.containsIgnoreCase(common))

            );
        }
        UserFilerDto userFilerDto = input.getFilter();
        if (!StringUtils.isEmpty(userFilerDto.getUserName())){
            condition = condition.and(USERS.USER_NAME.equalIgnoreCase(userFilerDto.getUserName()));
        }
        if (!StringUtils.isEmpty(userFilerDto.getEmail())){
            condition = condition.and(USERS.EMAIL.equalIgnoreCase(userFilerDto.getEmail()));
        }
        if(userFilerDto.getId()!=null && userFilerDto.getId()>0){
            condition = condition.and(USERS.ID.eq(userFilerDto.getId()));
        }
        if(userFilerDto.getIsLocked()!=null){
            condition = condition.and(USERS.IS_LOCKED.eq(userFilerDto.getIsLocked()));
        }
        if(userFilerDto.getCreatedFrom()!=null){
            condition = condition.and(USERS.CREATED_AT.ge(userFilerDto.getCreatedFrom()));
        }
        if(userFilerDto.getCreatedTo()!=null){
            condition = condition.and(USERS.CREATED_AT.le(userFilerDto.getCreatedTo()));
        }
        if(!StringUtils.isEmpty(userFilerDto.getRole())){
            condition = condition.and(ROLES.ROLE_NAME.eq(userFilerDto.getRole()));
        }
        selectQuery.addConditions(condition);
        long totalElement = dslContext.fetchCount(selectQuery);
        selectQuery.addOrderBy(input.getSort());
        if (pageable.isPaged()) {
            selectQuery.addOffset(pageable.getOffset());
            selectQuery.addLimit(pageable.getPageSize());
        }

        System.out.println(selectQuery.toString());
        return new PageImpl<>(selectQuery.fetchInto(UserDetailResponse.class), pageable, totalElement);

    }

    public PageImpl<UserCard> getAllUser(Pageable pageable, String commonSearch, Boolean asc, String sortProperty) {
        SelectQuery selectQuery = dslContext.select(
                        USERS.ID.as("id"),
                        USERS.USER_NAME.as("username"),
                        USERS.URL_AVATAR.as("urlAvatar")
                )
                .from(USERS)
                .leftJoin(USER_ROLE).on(USERS.ID.eq(USER_ROLE.USER_ID))
                .leftJoin(ROLES).on(USER_ROLE.ROLE_ID.eq(ROLES.ID))
                .groupBy(USERS.ID)
                .getQuery();
        Condition condition = DSL.noCondition();
        condition = condition.and(USERS.IS_LOCKED.eq(false));
        if (!StringUtils.isEmpty(commonSearch)) {
            condition = condition.and(USERS.USER_NAME.containsIgnoreCase(commonSearch)
                    .or(USERS.EMAIL.containsIgnoreCase(commonSearch))
                    .or(USERS.DESCRIPTION.containsIgnoreCase(commonSearch)));
        }
        selectQuery.addConditions(condition);
        long totalElements = dslContext.fetchCount(selectQuery);
        if (!StringUtils.isEmpty(sortProperty)) {
            OrderField orderField;
            if (sortProperty.equalsIgnoreCase( "userName")) {
                orderField = asc == true
                        ? DSL.field("username").asc()
                        : DSL.field("username").desc();
            } else {
                orderField = asc == true
                        ? DSL.field(camelToSnake("createdAt")).asc()
                        : DSL.field(camelToSnake("createdAt")).desc();
            }
            selectQuery.addOrderBy(orderField);
        }
        if(pageable.isPaged()){
            selectQuery.addOffset(pageable.getOffset());
            selectQuery.addLimit(pageable.getPageSize());
        }
        System.out.println(selectQuery.toString());
        return new PageImpl<>(selectQuery.fetchInto(UserCard.class), pageable, totalElements);
    }
    private String camelToSnake(String camelCase) {
        // Use a regular expression to find the positions where we need to insert underscores
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";

        // Replace the matches with the underscore and convert to lowercase
        String snakeCase = camelCase.replaceAll(regex, replacement).toLowerCase();

        return snakeCase;
    }
}
