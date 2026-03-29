package com.khoatrbl.blog.controller;

import com.khoatrbl.blog.domain.dtos.*;
import com.khoatrbl.blog.domain.entities.Post;
import com.khoatrbl.blog.domain.entities.User;
import com.khoatrbl.blog.mappers.PostMapper;
import com.khoatrbl.blog.services.PostService;
import com.khoatrbl.blog.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts(@RequestParam(required = false) UUID categoryId,
                                                     @RequestParam(required = false) UUID tagId) {
        List<Post> posts = postService.getAllPosts(categoryId, tagId);
        List<PostDto> postDtos = posts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(postDtos);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") UUID postId) {
        Post post = postService.getPost(postId);
        PostDto dto = postMapper.toDto(post);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostDto>> getDraftPosts(@RequestAttribute("userId") UUID userId) {
        User loggedInUser = userService.getUserById(userId);

        List<Post> drafts = postService.getDraftPosts(loggedInUser);
        List<PostDto> draftDtos = drafts.stream().map(postMapper::toDto).toList();

        return ResponseEntity.ok(draftDtos);
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody CreatePostRequestDto createPostRequestDto,
                                              @RequestAttribute("userId") UUID userId) {

        User loggedInUser = userService.getUserById(userId);

        CreatePostRequest request = postMapper.toCreatePostRequest(createPostRequestDto);

        Post post = postService.createPost(loggedInUser, request);
        PostDto postDto = postMapper.toDto(post);

        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable("id") UUID postId,
                                              @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        UpdatePostRequest request = postMapper.toUpdatePostRequest(updatePostRequestDto);

        Post updatedPost = postService.updatePost(postId, request);
        PostDto updatedPostDto = postMapper.toDto(updatedPost);

        return ResponseEntity.ok(updatedPostDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") UUID postId) {
        postService.deletePost(postId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPost() {
        postService.deleteAllPost();

        return ResponseEntity.noContent().build();
    }
}
