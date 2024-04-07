package com.project.socialnetwork.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.socialnetwork.entity.UserFriend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListFriendResponse {

    private Long total;
    private List<UserFriend> friends;
}
