package com.novelis.blog.service.serviceImp;

import com.novelis.blog.domain.Article;
import com.novelis.blog.dto.article.ArticleCreateRequest;
import com.novelis.blog.dto.article.ArticleResponse;
import com.novelis.blog.dto.article.ArticleUpdateRequest;
import com.novelis.blog.mapper.ArticleMapper;
import com.novelis.blog.repository.ArticleRepository;
import com.novelis.blog.service.ArticleService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;

    @Override
    public ArticleResponse create(UUID authorId, ArticleCreateRequest request) {
        var now = OffsetDateTime.now(ZoneOffset.UTC);

        String slug = generateUniqueSlug(request.getTitle());

        Article entity = Article.builder()
            .id(UUID.randomUUID())
            .authorId(authorId)
            .title(request.getTitle())
            .slug(slug)
            .content(request.getContent())
            .published(Boolean.TRUE.equals(request.getPublished()))
            .createdAt(now)
            .updatedAt(now)
            .build();

        Article saved = articleRepository.save(entity);
        return articleMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ArticleResponse getBySlug(String slug) {
        Article entity = articleRepository.findBySlug(slug)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));
        return articleMapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArticleResponse> listPublished(String query, Pageable pageable) {
        Page<Article> page;

        if (query == null || query.isBlank()) {
            page = articleRepository.findByPublishedTrue(pageable);
        } else {
            page = articleRepository.findByPublishedTrueAndTitleContainingIgnoreCase(query.trim(), pageable);
        }

        return page.map(articleMapper::toResponse);
    }

    @Override
    public ArticleResponse update(UUID id, ArticleUpdateRequest request) {
        Article entity = articleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));

        articleMapper.updateEntityFromDto(request, entity);

        // If title changes, we regenerate slug (optional behavior; you can lock slug if you prefer)
        entity.setSlug(generateUniqueSlug(entity.getTitle()));

        entity.setPublished(Boolean.TRUE.equals(request.getPublished()));
        entity.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        Article saved = articleRepository.save(entity);
        return articleMapper.toResponse(saved);
    }

    @Override
    public void delete(UUID id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article not found");
        }
        articleRepository.deleteById(id);
    }

    /**
     * Creates a URL-friendly slug and ensures uniqueness using repository checks.
     */
    private String generateUniqueSlug(String title) {
        String base = slugify(title);
        String slug = base;

        int i = 2;
        while (articleRepository.existsBySlug(slug)) {
            slug = base + "-" + i;
            i++;
        }
        return slug;
    }

    /**
     * Converts title to "hello-world" style slug.
     */
    private static String slugify(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        String slug = normalized
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9\\s-]", "")
            .trim()
            .replaceAll("\\s+", "-")
            .replaceAll("-{2,}", "-");

        return slug.isBlank() ? UUID.randomUUID().toString() : slug;
    }
}
