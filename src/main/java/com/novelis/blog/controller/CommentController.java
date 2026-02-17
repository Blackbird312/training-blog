package com.novelis.blog.controller;

import com.novelis.blog.repository.ArticleRepository;
import com.novelis.blog.dto.comment.CommentCreateRequest;
import com.novelis.blog.dto.comment.CommentResponse;
import com.novelis.blog.service.CommentService;
import com.novelis.blog.service.UserResolverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/v1/articles/{slug}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final ArticleRepository articleRepository;
    private final UserResolverService userResolverService;

    @GetMapping
    public ResponseEntity<Page<CommentResponse>> list(
        @PathVariable String slug,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        UUID articleId = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"))
            .getId();

        return ResponseEntity.ok(commentService.listByArticle(articleId, pageable));
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(
        @PathVariable String slug,
        @Valid @RequestBody CommentCreateRequest request,
        Authentication authentication
    ) {
        UUID articleId = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"))
            .getId();

        UUID authorId = userResolverService.resolveLocalUserId(authentication);

        return ResponseEntity.ok(commentService.create(articleId, authorId, request));
    }
}
