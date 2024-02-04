package org.gsmapp.service;

import org.gsmapp.dto.UserDto;
import org.gsmapp.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    UserDto create(UserDto user);

    UserEntity getUserById(UUID uuid);
}