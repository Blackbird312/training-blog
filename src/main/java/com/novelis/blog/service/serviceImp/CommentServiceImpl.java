package com.novelis.blog.service.serviceImp;

import com.novelis.blog.repository.ArticleRepository;
import com.novelis.blog.domain.Comment;
import com.novelis.blog.dto.comment.CommentCreateRequest;
import com.novelis.blog.dto.comment.CommentResponse;
import com.novelis.blog.exception.NotFoundException;
import com.novelis.blog.mapper.CommentMapper;
import com.novelis.blog.repository.CommentRepository;
import com.novelis.blog.service.CommentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse create(UUID articleId, UUID authorId, CommentCreateRequest request) {

        // Ensure article exists
        articleRepository.findById(articleId)
            .orElseThrow(() -> new NotFoundException("Article not found"));

        Comment comment = Comment.builder()
            .id(UUID.randomUUID())
            .articleId(articleId)
            .authorId(authorId)
            .content(request.getContent())
            .createdAt(OffsetDateTime.now(ZoneOffset.UTC))
            .build();

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> listByArticle(UUID articleId, Pageable pageable) {
        return commentRepository
            .findByArticleId(articleId, pageable)
            .map(commentMapper::toResponse);
    }
}
