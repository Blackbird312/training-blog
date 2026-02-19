package com.novelis.blog.dto.comment;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.novelis.blog.dto.user.UserAuthorDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private UUID id;
    // private UUID authorId;
    private UserAuthorDto author;
    private String content;
    private OffsetDateTime createdAt;
}
