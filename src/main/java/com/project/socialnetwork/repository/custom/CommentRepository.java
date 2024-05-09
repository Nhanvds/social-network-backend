package com.project.socialnetwork.repository.custom;

import com.project.socialnetwork.response.PostCommentResponse;
import com.project.socialnetwork.response.UserCard;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.jooq.impl.DSL;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.project.socialnetwork.database.Tables.*;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final DSLContext dslContext;

    public PageImpl<PostCommentResponse> getCommentsByPostId(Long postId, Pageable pageable){
        SelectQuery selectQuery = dslContext
                .select(
                        POST_COMMENTS.ID,
                        POST_COMMENTS.CONTENT,
                        DSL.row(
                                USERS.ID.as("userId"),
                                USERS.USER_NAME.as("username"),
                                USERS.URL_AVATAR.as("urlAvatar")
                        ).convertFrom(r->r.into(UserCard.class)).as("user"),
                        POST_COMMENTS.CREATED_AT.as("createdTime")
                )
                .from(POST_COMMENTS)
                .leftJoin(USERS).on(USERS.ID.eq(POST_COMMENTS.USER_ID))
                .where(POST_COMMENTS.POST_ID.eq(postId))
                .getQuery()
                ;
        long totalCount = selectQuery.stream().count();
        if(pageable.isPaged()){
            Sort sort = pageable.getSort();
            selectQuery.addLimit(pageable.getPageSize());
            selectQuery.addOffset(pageable.getOffset());
            selectQuery.addOrderBy(
                    sort.stream().findFirst().get().isAscending()
                    ?DSL.field(POST_COMMENTS.CREATED_AT).asc()
                            :DSL.field(POST_COMMENTS.CREATED_AT).desc()
            );
        }
        List<PostCommentResponse> postCommentResponseList = selectQuery.fetchInto(PostCommentResponse.class);
        return new PageImpl<PostCommentResponse>(postCommentResponseList,pageable,totalCount);
    }
}
