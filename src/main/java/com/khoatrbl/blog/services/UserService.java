package com.khoatrbl.blog.services;

import com.khoatrbl.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
