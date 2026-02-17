package com.novelis.blog.repository;

import com.novelis.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Article> findByPublishedTrue(Pageable pageable);

    Page<Article> findByPublishedTrueAndTitleContainingIgnoreCase(String query, Pageable pageable);
}
