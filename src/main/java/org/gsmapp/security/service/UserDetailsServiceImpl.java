package org.gsmapp.security.service;

import lombok.RequiredArgsConstructor;
import org.gsmapp.entity.UserEntity;
import org.gsmapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .stream()
                .findFirst()
                .map(u -> u.orElseThrow(() -> new UsernameNotFoundException("User is not present")))
                .map(UserPrincipal::create)
                .orElseThrow(() -> new UsernameNotFoundException(username + " does not exist"));
    }
}
