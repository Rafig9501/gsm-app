package org.gsmapp.security.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gsmapp.security.token.JwtTokenService;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.gsmapp.util.Constants.AUTHORIZATION;
import static org.gsmapp.util.Constants.BEARER;


public class AuthorizationTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public AuthorizationTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (filterChain == null || request == null) {
            return;
        }
        Optional<String> tokenOpt = Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER))
                .map(authHeader -> authHeader.substring(7));
        if (tokenOpt.isEmpty()) {
                filterChain.doFilter(request, response);
            return;
        }
        final String token = tokenOpt.get();
        if (jwtTokenService.tokenIsValid(tokenOpt.get())) {
            jwtTokenService.getOwner(token).ifPresent(owner -> {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(owner.getUsername(), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }
        filterChain.doFilter(request, response);
    }
}