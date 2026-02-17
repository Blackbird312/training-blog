package com.novelis.blog.service;
import com.novelis.blog.dto.article.ArticleCreateRequest;
import com.novelis.blog.dto.article.ArticleResponse;
import com.novelis.blog.dto.article.ArticleUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ArticleService {

    ArticleResponse create(UUID authorId, ArticleCreateRequest request);

    ArticleResponse getBySlug(String slug);

    Page<ArticleResponse> listPublished(String query, Pageable pageable);

    ArticleResponse update(UUID id, ArticleUpdateRequest request);

    void delete(UUID id);
}
