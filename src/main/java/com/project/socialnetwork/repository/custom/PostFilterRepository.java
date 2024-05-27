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

    /**
     * ADMIN
     *
     * @param input
     * @param myId
     * @param pageable
     * @return danh sách bài viết lọc theo thuộc tính đưa vào.
     */
    public PageImpl<PostResponse> searchPost(PageFilterDto<PostFilterDto> input, Long myId, Pageable pageable) {


        SelectQuery selectQuery = dslContext.select(
                        POSTS.ID,
                        POSTS.CONTENT,
                        multiset(
                                select(POST_IMAGES.URL_IMAGE).from(POST_IMAGES)
                                        .where(POST_IMAGES.POST_ID.eq(POSTS.ID))
                        ).as("postImages").convertFrom(r -> r.into(String.class)),
                        POST_PRIVACY_STATUS.NAME.as("postPrivacyStatus"),
                        POSTS.CREATED_TIME.as("createdTime"),
                        POSTS.UPDATED_TIME.as("updatedTime"),
                        POSTS.IS_LOCKED.as("isLocked"),
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
                .groupBy(POSTS.ID).getQuery();

        Condition condition = DSL.noCondition();
        if (!StringUtils.isEmpty(input.getCommon())) {
            condition = condition.and(
                    POSTS.CONTENT.containsIgnoreCase(input.getCommon().strip())
                            .or(USERS.USER_NAME.containsIgnoreCase(input.getCommon().strip()))
            );
        }

        PostFilterDto postFilterDto = input.getFilter();

        if (postFilterDto != null) {
            if (postFilterDto.getIsLocked() != null) {
                condition = condition.and(POSTS.IS_LOCKED.eq(postFilterDto.getIsLocked()));
            }
            if (!StringUtils.isEmpty(postFilterDto.getContent())) {
                condition = condition.and(POSTS.CONTENT.equalIgnoreCase(postFilterDto.getContent().strip()));
            }

            if (postFilterDto.getCreatedTimeFrom() != null) {
                condition = condition.and(POSTS.CREATED_TIME.ge(postFilterDto.getCreatedTimeFrom()));
            }
            if (postFilterDto.getCreatedTimeTo() != null) {
                condition = condition.and(POSTS.CREATED_TIME.le(postFilterDto.getCreatedTimeTo()));
            }
            if (postFilterDto.getPostPrivacy() != null) {
                if (postFilterDto.getPostPrivacy() == true) {
                    condition = condition.and(POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PUBLIC));
                } else {
                    condition = condition.and(POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PRIVATE));
                }
            }
            if(postFilterDto.getUserId()!=null){
                condition = condition.and(POSTS.USER_ID.eq(postFilterDto.getUserId()));
            }
            if(postFilterDto.getPostId()!=null){
                condition = condition.and((POSTS.ID.eq(postFilterDto.getPostId())));
            }

        }

        selectQuery.addConditions(condition);
        long totalCount = dslContext.fetchCount(selectQuery);
        if (input.getAsc() == null || input.getAsc()) {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).asc();
            selectQuery.addOrderBy(orderField);
        } else {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).desc();
            selectQuery.addOrderBy(orderField);
        }


        if (pageable.isPaged()) {
            selectQuery.addLimit(pageable.getPageSize());
            selectQuery.addOffset(pageable.getOffset());
        }

        List<PostResponse> postResponses = selectQuery.fetchInto(PostResponse.class);
        System.out.println(selectQuery.toString());

        return new PageImpl<PostResponse>(postResponses, pageable, totalCount);


    }

    /**
     * @param pageable
     * @param myId
     * @param asc
     * @param common
     * @param hasLiked
     * @return Lấy danh sách bài viết của người dùng đăng nhập và của bạn bè. Sort theo thời gian tạo
     */
    public PageImpl<PostResponse> getPostsInHome(
            Pageable pageable,
            Long myId,
            Boolean asc,
            String common,
            Boolean hasLiked) {
        SelectQuery selectQuery = dslContext.select(
                        POSTS.ID,
                        POSTS.CONTENT,
                        multiset(
                                select(POST_IMAGES.URL_IMAGE).from(POST_IMAGES)
                                        .where(POST_IMAGES.POST_ID.eq(POSTS.ID))
                        ).as("postImages").convertFrom(r -> r.into(String.class)),
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
                .groupBy(POSTS.ID).getQuery();

        Condition condition = DSL.noCondition();
        condition = condition.and(POSTS.IS_LOCKED.isFalse());
        condition = condition.and(USERS.IS_LOCKED.eq(false));
        condition = condition.and(POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PUBLIC)
                .or(POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PRIVATE).and(POSTS.USER_ID.eq(myId))));
        if (!StringUtils.isEmpty(common)) {
            condition = condition.and(
                    POSTS.CONTENT.containsIgnoreCase(common.strip())
            );
        }
        if (hasLiked != null && hasLiked == true) {
            condition = condition.and(POST_REACTIONS.HAS_LIKED.eq(hasLiked).and(POST_REACTIONS.USER_ID.eq(myId)));
        }
        selectQuery.addConditions(condition);

        if (asc == null || asc == true) {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).asc();
            selectQuery.addOrderBy(orderField);
        } else {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).desc();
            selectQuery.addOrderBy(orderField);
        }
        long totalCount = dslContext.fetchCount(selectQuery);

        if (pageable.isPaged()) {
            selectQuery.addLimit(pageable.getPageSize());
            selectQuery.addOffset(pageable.getOffset());
        }

        List<PostResponse> postResponses = selectQuery.fetchInto(PostResponse.class);
        System.out.println(selectQuery.toString());

        return new PageImpl<PostResponse>(postResponses, pageable, totalCount);
    }

    /**
     * @param pageable
     * @param myId
     * @param asc
     * @return Lấy danh sách bài viết theo userId, nếu userId==myId thì lấy cả public,private. Sort theo thời gian tạo
     */
    public PageImpl<PostResponse> getPostsByUserId(
            Pageable pageable,
            Long myId,
            Long userId,
            Boolean asc,
            String common,
            Boolean hasLiked) {
        SelectQuery selectQuery = dslContext.select(
                        POSTS.ID,
                        POSTS.CONTENT,
                        multiset(
                                select(POST_IMAGES.URL_IMAGE).from(POST_IMAGES)
                                        .where(POST_IMAGES.POST_ID.eq(POSTS.ID))
                        ).as("postImages").convertFrom(r -> r.into(String.class)),
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
                .groupBy(POSTS.ID).getQuery();

        Condition condition = DSL.noCondition();
        condition = condition.and(USERS.IS_LOCKED.eq(false));
        condition = condition.and(POSTS.IS_LOCKED.isFalse());
        if(myId==userId){
            condition = condition.and(POSTS.USER_ID.eq(myId));

        }else {
            condition = condition.and(POSTS.USER_ID.eq(userId));
            condition = condition.and(POST_PRIVACY_STATUS.NAME.eq(PostPrivacyStatus.PRIVACY_PUBLIC));
        }
        if (!StringUtils.isEmpty(common)) {
            condition = condition.and(
                    POSTS.CONTENT.containsIgnoreCase(common.strip())
            );
        }
        if (hasLiked != null && hasLiked == true) {
            condition = condition.and(POST_REACTIONS.HAS_LIKED.eq(hasLiked).and(POST_REACTIONS.USER_ID.eq(myId)));
        }
        selectQuery.addConditions(condition);

        if (asc == null || asc == true) {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).asc();
            selectQuery.addOrderBy(orderField);
        } else {
            OrderField orderField = DSL.field(POSTS.CREATED_TIME).desc();
            selectQuery.addOrderBy(orderField);
        }
        long totalCount = selectQuery.stream().count();

        if (pageable.isPaged()) {
            selectQuery.addLimit(pageable.getPageSize());
            selectQuery.addOffset(pageable.getOffset());
        }

        List<PostResponse> postResponses = selectQuery.fetchInto(PostResponse.class);
        System.out.println(selectQuery.toString());

        return new PageImpl<PostResponse>(postResponses, pageable, totalCount);
    }

}
