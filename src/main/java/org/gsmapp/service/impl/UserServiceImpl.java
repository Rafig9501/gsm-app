package org.gsmapp.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.gsmapp.exception.EntityNotFoundException;
import org.gsmapp.exception.UserAlreadyExistsException;
import org.gsmapp.dto.UserDto;
import org.gsmapp.entity.UserEntity;
import org.gsmapp.mapper.UserMapper;
import org.gsmapp.repository.UserRepository;
import org.gsmapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto create(UserDto user) {
        String username = ObjectUtils.firstNonNull(user.getUsername());
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists!");
        }
        final String encodedPassword = passwordEncoder.encode(user.getPassword());
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(encodedPassword);
        userEntity.setUsername(user.getUsername());
        userEntity = userRepository.save(userEntity);
        return userMapper.userEntityToUserDto(userEntity);
    }

    @SneakyThrows
    @Override
    public UserEntity getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() ->
                new EntityNotFoundException("There is no user with given userId"));
    }
}
