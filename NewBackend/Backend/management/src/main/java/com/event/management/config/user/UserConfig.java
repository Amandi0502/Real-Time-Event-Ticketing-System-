package com.event.management.config.user;
import com.event.management.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor                               //can inject the constructor injection
public class UserConfig implements UserDetails {

    private final UserEntity userEntity;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(userEntity
                        .getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUserName();
    }

    public String getMobileNumber(){return  userEntity.getMobileNumber();}

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
