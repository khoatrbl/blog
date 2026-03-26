package com.khoatrbl.blog.controller;

import com.khoatrbl.blog.domain.dtos.CreateTagRequest;
import com.khoatrbl.blog.domain.dtos.TagDto;
import com.khoatrbl.blog.domain.entities.Tag;
import com.khoatrbl.blog.mappers.TagMapper;
import com.khoatrbl.blog.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getTags()
                .stream()
                .map(tagMapper::toTagDto)
                .toList();

        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<List<TagDto>> createTags(@Valid @RequestBody CreateTagRequest createTagRequest) {
        List<Tag> savedTags = tagService.createTags(createTagRequest.getTagNames());

        List<TagDto> createdTagDtos = savedTags.stream()
                .map(tagMapper::toTagDto)
                .toList();

        return new ResponseEntity<>(createdTagDtos, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable("id") UUID tagId) {
        tagService.deleteTag(tagId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
