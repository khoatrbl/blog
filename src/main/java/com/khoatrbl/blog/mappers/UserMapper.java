package com.khoatrbl.blog.mappers;

import com.khoatrbl.blog.domain.PostStatus;
import com.khoatrbl.blog.domain.dtos.UserDto;
import com.khoatrbl.blog.domain.entities.Post;
import com.khoatrbl.blog.domain.entities.User;
import jdk.jfr.Name;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    UserDto toDto(User user);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {
        if (posts == null) {
            return 0;
        }

        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISHED))
                .count();
    }
}
