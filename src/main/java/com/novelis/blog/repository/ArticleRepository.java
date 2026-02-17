package com.novelis.blog.repository;

import com.novelis.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository extends JpaRepository<Article, UUID> {

    Optional<Article> findBySlug(String slug);

    boolean existsBySlug(String slug);

    Page<Article> findByPublishedTrue(Pageable pageable);

    Page<Article> findByPublishedTrueAndTitleContainingIgnoreCase(String query, Pageable pageable);

    @Query("""
                SELECT a
                FROM Article a
                WHERE a.published = true
                  AND (
                    LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%'))
                    OR LOWER(a.content) LIKE LOWER(CONCAT('%', :q, '%'))
                  )
            """)
    Page<Article> searchPublishedByKeyword(@Param("q") String q, Pageable pageable);
}
