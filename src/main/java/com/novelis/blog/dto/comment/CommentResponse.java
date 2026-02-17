package com.novelis.blog.dto.comment;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {

    private UUID id;
    private UUID authorId;
    private String content;
    private OffsetDateTime createdAt;
}
