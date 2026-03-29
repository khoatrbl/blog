package com.khoatrbl.blog.services.impl;

import com.khoatrbl.blog.domain.PostStatus;
import com.khoatrbl.blog.domain.dtos.CreatePostRequest;
import com.khoatrbl.blog.domain.dtos.UpdatePostRequest;
import com.khoatrbl.blog.domain.entities.Category;
import com.khoatrbl.blog.domain.entities.Post;
import com.khoatrbl.blog.domain.entities.Tag;
import com.khoatrbl.blog.domain.entities.User;
import com.khoatrbl.blog.repositories.PostRepository;
import com.khoatrbl.blog.services.CategoryService;
import com.khoatrbl.blog.services.PostService;
import com.khoatrbl.blog.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;
    private static final int WORDS_PER_MINUTE = 200;

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPosts(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.getCategoryById(categoryId);
            Tag tag = tagService.getTagById(tagId);

            return
                    postRepository.findAllByStatusAndCategoryAndTagsContaining(
                            PostStatus.PUBLISHED,
                            category,
                            tag);
        }

        if (categoryId != null) {
            Category category = categoryService.getCategoryById(categoryId);

            return postRepository.findAllByStatusAndCategoryContaining(PostStatus.PUBLISHED, category);

        }

        if (tagId != null) {
            Tag tag = tagService.getTagById(tagId);

            return postRepository.findAllByStatusAndTagsContaining(PostStatus.PUBLISHED, tag);
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }

    @Override
    public Post getPost(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " does not exist."));
    }

    @Override
    public List<Post> getDraftPosts(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Transactional
    @Override
    public Post createPost(User user, CreatePostRequest request) {
        Category category = categoryService.getCategoryById(request.getCategoryId());

        Set<UUID> tagIds = request.getTagIds();
        List<Tag> tags = tagService.getTagsByIds(tagIds);

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(user)
                .category(category)
                .tags(new HashSet<>(tags))
                .status(request.getStatus())
                .readingTime(calculateReadingTime(request.getContent()))
                .build();

        return postRepository.save(post);
    }

    @Override
    public Post updatePost(UUID postId, UpdatePostRequest request) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id: " + postId));

        String newContent = request.getContent();

        existingPost.setTitle(request.getTitle());
        existingPost.setContent(newContent);
        existingPost.setStatus(request.getStatus());
        existingPost.setReadingTime(calculateReadingTime(newContent));

        UUID updatedCategoryId = request.getCategoryId();

        if (!existingPost.getCategory().getCategoryId().equals(updatedCategoryId)) {
            Category newCategory = categoryService.getCategoryById(updatedCategoryId);

            existingPost.setCategory(newCategory);
        }

        Set<UUID> existingTagIds = existingPost.getTags().stream().map(Tag::getTagId).collect(Collectors.toSet());
        Set<UUID> updatedTagIds = request.getTagIds();

        if (!existingTagIds.equals(updatedTagIds)) {
            List<Tag> newTags = tagService.getTagsByIds(updatedTagIds);

            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);

    }

    @Override
    public void deletePost(UUID postId) {
        Post post = getPost(postId);

        postRepository.delete(post);
    }

    @Override
    public void deleteAllPost() {
        postRepository.deleteAll();
    }

    private int calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;

        return (int) Math.ceil((double)wordCount / WORDS_PER_MINUTE);
    }
}
