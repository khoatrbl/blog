package com.khoatrbl.blog.services.impl;

import com.khoatrbl.blog.domain.entities.Tag;
import com.khoatrbl.blog.repositories.TagRepository;
import com.khoatrbl.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByTagNameIn(tagNames);

        Set<String> existingTagNames = existingTags.stream()
                        .map(Tag::getTagName)
                        .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .tagName(name)
                        .posts(new HashSet<>())
                        .build())
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Override
    public void deleteTag(UUID tagId) {
        tagRepository.findById(tagId).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Cannot delete tag with posts.");
            }
            tagRepository.deleteById(tagId);
        });
    }

    @Override
    public Tag getTagById(UUID tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
    }

    @Override
    public List<Tag> getTagsByIds(Set<UUID> tagIds) {
        List<Tag> foundTags = tagRepository.findAllById(tagIds);

        if (foundTags.size() != tagIds.size()) {
            throw new EntityNotFoundException("Not all specified tag ids exist");
        }

        return foundTags;
    }
}
