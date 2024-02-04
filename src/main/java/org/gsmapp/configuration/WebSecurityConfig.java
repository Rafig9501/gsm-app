package org.gsmapp.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.gsmapp.security.service.UserDetailsServiceImpl;
import org.gsmapp.security.token.JwtTokenService;
import org.gsmapp.security.web.AuthorizationTokenFilter;
import org.gsmapp.security.web.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

import static org.gsmapp.util.Constants.MONITORING_PATHS;
import static org.gsmapp.util.Constants.ALLOWED_PATHS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final CustomAuthenticationEntryPoint unauthorizedHandler;
    private final JwtTokenService jwtTokenService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r
                        .requestMatchers(getPublicRoutes())
                        .permitAll()
                        .requestMatchers(MONITORING_PATHS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler))
                .addFilterBefore(authorizationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationManager = new DaoAuthenticationProvider();
        authenticationManager.setUserDetailsService(userDetailsServiceImpl);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(Collections.singletonList(authenticationManager));
    }

    @Bean
    public AuthorizationTokenFilter authorizationTokenFilter() {
        return new AuthorizationTokenFilter(jwtTokenService);
    }

    private String[] getPublicRoutes() {
        return ArrayUtils.addAll(ALLOWED_PATHS);
    }
}
