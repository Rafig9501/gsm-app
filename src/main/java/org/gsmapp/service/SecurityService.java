package org.gsmapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.gsmapp.dto.TokenDto;
import org.gsmapp.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface SecurityService {

    TokenDto createAuthenticationTokenPair(String username, String password);

    TokenDto refreshToken(HttpServletRequest request);
}