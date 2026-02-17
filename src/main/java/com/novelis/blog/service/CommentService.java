package com.novelis.blog.service;

import com.novelis.blog.dto.comment.CommentCreateRequest;
import com.novelis.blog.dto.comment.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CommentService {

    CommentResponse create(UUID articleId, UUID authorId, CommentCreateRequest request);

    Page<CommentResponse> listByArticle(UUID articleId, Pageable pageable);
}
