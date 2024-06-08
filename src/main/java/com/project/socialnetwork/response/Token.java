package com.project.socialnetwork.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    private String token;
    private String refreshToken;
}
