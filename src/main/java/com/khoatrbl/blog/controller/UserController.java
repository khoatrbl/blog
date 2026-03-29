package com.khoatrbl.blog.controller;

import com.khoatrbl.blog.domain.dtos.UserDto;
import com.khoatrbl.blog.domain.entities.User;
import com.khoatrbl.blog.mappers.UserMapper;
import com.khoatrbl.blog.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getUsers();

        List<UserDto> userDtos = users.stream().map(userMapper::toDto).toList();

        return ResponseEntity.ok(userDtos);
    }
}
