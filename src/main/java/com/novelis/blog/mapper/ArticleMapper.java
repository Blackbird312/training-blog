package com.novelis.blog.mapper;

import com.novelis.blog.domain.Article;
import com.novelis.blog.dto.article.ArticleResponse;
import com.novelis.blog.dto.article.ArticleUpdateRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleResponse toResponse(Article entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ArticleUpdateRequest dto, @MappingTarget Article entity);

    // CreateRequest → Entity is usually done in service because we must set:
    // id, authorId, slug, createdAt, updatedAt.
}
