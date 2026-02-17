package com.novelis.blog.controller;

import com.novelis.blog.dto.article.ArticleCreateRequest;
import com.novelis.blog.dto.article.ArticleResponse;
import com.novelis.blog.dto.article.ArticleUpdateRequest;
import com.novelis.blog.service.ArticleService;
import com.novelis.blog.service.UserResolverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.novelis.blog.consts.consts;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(consts.API_URI + "/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final UserResolverService userResolverService;

    /**
     * Public list (published only).
     * GET /articles?q=keyword&page=0&size=10&sort=createdAt,desc
     */
    @GetMapping
    public ResponseEntity<Page<ArticleResponse>> listPublished(
        @RequestParam(name = "q", required = false) String query,
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(articleService.listPublished(query, pageable));
    }

    /**
     * Public read by slug.
     * GET /articles/{slug}
     */
    @GetMapping("/{slug}")
    public ResponseEntity<ArticleResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getBySlug(slug));
    }

    /**
     * Protected: AUTHOR/ADMIN (configured in SecurityConfig)
     * POST /articles
     */
    @PostMapping
    public ResponseEntity<ArticleResponse> create(
        @Valid @RequestBody ArticleCreateRequest request,
        Authentication authentication
    ) {
        UUID authorId = userResolverService.resolveLocalUserId(authentication);

        ArticleResponse created = articleService.create(authorId, request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{slug}")
            .buildAndExpand(created.getSlug())
            .toUri();

        return ResponseEntity.created(location).body(created);
    }

    /**
     * Protected: AUTHOR/ADMIN
     * PUT /articles/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> update(
        @PathVariable UUID id,
        @Valid @RequestBody ArticleUpdateRequest request
    ) {
        return ResponseEntity.ok(articleService.update(id, request));
    }

    /**
     * Protected: ADMIN
     * DELETE /articles/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
