package com.khoatrbl.blog.services.impl;

import com.khoatrbl.blog.domain.entities.Tag;
import com.khoatrbl.blog.repositories.TagRepository;
import com.khoatrbl.blog.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public List<Tag> getTags() {
        return tagRepository.findAllWithPostCount();
    }
}
