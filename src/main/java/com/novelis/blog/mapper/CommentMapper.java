package com.novelis.blog.mapper;

import com.novelis.blog.domain.Comment;
import com.novelis.blog.domain.User;
import com.novelis.blog.dto.comment.CommentResponse;
import com.novelis.blog.dto.user.UserAuthorDto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponse toResponse(Comment entity);
    UserAuthorDto toAuthorDto(User user);
    
}
