package com.novelis.blog.dto.article;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResponse {

    private UUID id;
    private UUID authorId;

    private String title;
    private String slug;
    private String content;

    private boolean published;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
