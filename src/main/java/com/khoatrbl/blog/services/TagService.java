package com.khoatrbl.blog.services;

import com.khoatrbl.blog.domain.entities.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> getTags();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID tagId);
    Tag getTagById(UUID tagId);
    List<Tag> getTagsByIds(Set<UUID> tagIds);
}
