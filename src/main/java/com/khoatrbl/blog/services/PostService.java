package com.khoatrbl.blog.services;

import com.khoatrbl.blog.domain.dtos.CreatePostRequest;
import com.khoatrbl.blog.domain.dtos.UpdatePostRequest;
import com.khoatrbl.blog.domain.entities.Post;
import com.khoatrbl.blog.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts(UUID categoryId, UUID tagId);
    List<Post> getDraftPosts(User user);
    Post createPost(User user, CreatePostRequest request);
    Post updatePost(UUID postId, UpdatePostRequest request);
}
