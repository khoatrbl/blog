package com.khoatrbl.blog.mappers;

import com.khoatrbl.blog.domain.PostStatus;
import com.khoatrbl.blog.domain.dtos.TagDto;
import com.khoatrbl.blog.domain.entities.Post;
import com.khoatrbl.blog.domain.entities.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {
    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    TagDto toTagDto(Tag tag);

    @Named("calculatePostCount")
    default long calculatePostCount(Set<Post> posts) {
        if (posts == null) {
            return 0L;
        }

        return posts.stream()
                .filter(post -> PostStatus.PUBLISHED.equals(post.getStatus()))
                .count();
    }

}