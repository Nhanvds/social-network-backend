package com.project.socialnetwork.repository.custom;

import com.project.socialnetwork.dto.filter.PageFilterDto;
import com.project.socialnetwork.dto.filter.PostFilterDto;
import com.project.socialnetwork.entity.PostPrivacyStatus;
import com.project.socialnetwork.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.project.socialnetwork.database.Tables.*;
import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class PostFilterRepository {
    private final DSLContext dslContext;

    public PageImpl<PostResponse> searchPost(PageFilterDto<PostFilterDto> input, Long myId, Pageable pageable) {


        SelectQuery selectQuery = dslContext.select(
                        POSTS.ID,
                        POSTS.CONTENT,
                        multiset(
                                select(POST_IMAGES.URL_IMAGE).from(POST_IMAGES)
                                        .where(POST_IMAGES.POST_ID.eq(POSTS.ID))
                        ).as("postImages").convertFrom(r->r.into(String.class)),
                        POST_PRIVACY_STATUS.NAME.as("postPrivacyStatus"),
                        POSTS.CREATED_TIME.as("createdTime"),
                        POSTS.UPDATED_TIME.as("updatedTime"),
                        USERS.ID.as("userId"),
                        USERS.USER_NAME.as("username"),
                        USERS.URL_AVATAR.as("urlAvatar"),
                        count().filterWhere(POST_REACTIONS.HAS_LIKED.isTrue().and(POST_REACTIONS.POST_ID.eq(POSTS.ID))).as("likedReactions"),
                        count().filterWhere(POST_REACTIONS.HAS_LIKED.isFalse().and(POST_REACTIONS.POST_ID.eq(POSTS.ID))).as("dislikedReactions"),
                        exists(selectOne().from(POST_REACTIONS)
                                .where(POST_REACTIONS.USER_ID.eq(myId).and(POST_REACTIONS.HAS_LIKED.isTrue()).and(POST_REACTIONS.POST_ID.eq(POSTS.ID)))).as("hasLiked"),
                        exists(selectOne().from(POST_REACTIONS)
                                .where(POST_REACTIONS.USER_ID.eq(myId).and(POST_REACTIONS.HAS_LIKED.isFalse()).and(POST_REACTIONS.POST_ID.eq(POSTS.ID)))).as("hasDisLiked")
                )
                .from(POSTS)
                .leftJoin(USERS).on(USERS.ID.eq(POSTS.USER_ID))
                .leftJoin(POST_PRIVACY_STATUS).on(POST_PRIVACY_STATUS.ID.eq(POSTS.POST_PRIVACY_STATUS_ID))
                .leftJoin(POST_REACTIONS).on(POST_REACTIONS.POST_ID.eq(POSTS.ID))
                .leftJoin(POST_USER_STATUS).on(POST_USER_STATUS.POST_ID.eq(POSTS.ID))
                .groupBy(POSTS.ID).getQuery();

        Condition condition = DSL.noCondition();
        if (!StringUtils.isEmpty(input.getCommon())) {
            condition = condition.or(
                    POSTS.CONTENT.containsIgnoreCase(input.getCommon())
            );
        }

        PostFilterDto postFilterDto = input.getFilter();

        if (postFilterDto != null) {
            if (postFilterDto.getIsLocked() != null) {
                condition = condition.and(POSTS.IS_LOCKED.eq(postFilterDto.getIsLocked()));
            }
            if (!StringUtils.isEmpty(postFilterDto.getContent())) {
                condition = condition.and(POSTS.CONTENT.eq(postFilterDto.getContent()));
            }
            // nếu userId == myId lấy các bài viết đã đăng
            if (postFilterDto.getUserId() != null) {
                Long userId = postFilterDto.getUserId();
                condition = condition.and(POSTS.USER_ID.eq(postFilterDto.getUserId()));
                if (userId != myId) {
                    condition = condition.and((POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PUBLIC)));
                }
            }else{
                condition=condition.and(POSTS.USER_ID.ne(myId));
            }
            if (postFilterDto.getHasSeen() != null) {
                condition = condition.and(POST_USER_STATUS.HAS_SEEN.eq(postFilterDto.getHasSeen()).and(POST_USER_STATUS.USER_ID.eq(myId)));
            }
            if (postFilterDto.getHasLiked() != null) {
                condition = condition.and(POST_REACTIONS.HAS_LIKED.eq(postFilterDto.getHasLiked()).and(POST_REACTIONS.USER_ID.eq(myId)));
            }
            if (postFilterDto.getCreatedTimeFrom() != null) {
                condition = condition.and(POSTS.CREATED_TIME.ge(postFilterDto.getCreatedTimeFrom()));
            }
            if (postFilterDto.getCreatedTimeTo() != null) {
                condition = condition.and(POSTS.CREATED_TIME.le(postFilterDto.getCreatedTimeTo()));
            }

        }

        selectQuery.addConditions(condition);

        if (input.getAsc() == null || input.getAsc()) {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).asc();
            selectQuery.addOrderBy(orderField);
        } else {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).desc();
            selectQuery.addOrderBy(orderField);
        }
        long totalCount = selectQuery.stream().count();

        if (pageable.isPaged()) {
            selectQuery.addLimit(pageable.getPageSize());
            selectQuery.addOffset(pageable.getPageNumber());
        }

        List<PostResponse> postResponses = selectQuery.fetchInto(PostResponse.class);
        System.out.println(selectQuery.toString());

        return new PageImpl<PostResponse>(postResponses, pageable, totalCount);


    }
}
