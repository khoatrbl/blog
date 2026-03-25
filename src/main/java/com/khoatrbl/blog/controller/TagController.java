package com.khoatrbl.blog.controller;

import com.khoatrbl.blog.domain.dtos.TagDto;
import com.khoatrbl.blog.mappers.TagMapper;
import com.khoatrbl.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
