package org.gsmapp.security.service;

import lombok.Data;
import org.gsmapp.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;


@Data
public class UserPrincipal implements UserDetails {

    private final UUID id;
    private final String password;
    private final String username;

    private UserPrincipal(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    static UserPrincipal create(UserEntity user) {
        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
