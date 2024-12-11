package com.event.management.config.jwtConfig;

import com.event.management.config.user.UserConfig;
import com.event.management.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public String getEmail(Jwt jwtToken) {
        return jwtToken.getClaim("email");
    }
    public Long getUserId(Jwt jwtToken) {
        return jwtToken.getClaim("user_id");
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){    //check Token username is same or not in the DB username
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;

    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    private final UserRepo userRepo;
    public UserDetails userDetails(String emailId){
        return userRepo
                .findByEmailId(emailId)
                .map(UserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+emailId+" does not exist"));
    }

}

