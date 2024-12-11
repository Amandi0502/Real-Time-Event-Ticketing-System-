package com.event.management.config.jwtConfig;

import com.event.management.config.RSAKeyRecord;
import com.event.management.dto.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try{
            log.info("[JwtAccessTokenFilter:doFilterInternal] :: Started ");

            log.info("[JwtAccessTokenFilter:doFilterInternal]Filtering the Http Request:{}",request.getRequestURI());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.info("authHeader: {}", authHeader);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("Authorization header is missing or invalid.");
                filterChain.doFilter(request, response); // Continue without authentication
                return;
            }
            JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

            if(!authHeader.startsWith(TokenType.Bearer.name())){
                // Handle missing or invalid token
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Token is missing or invalid.");
                return;
            }

            final String token = authHeader.substring(7);
            log.info("[token " + token);
            final Jwt jwtToken = jwtDecoder.decode(token);      //decode the JWT token
            log.info("[jwtToken " + jwtToken);

            final String email_id = jwtTokenUtils.getEmail(jwtToken);

            if(!email_id.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails = jwtTokenUtils.userDetails(email_id);
                if(jwtTokenUtils.isTokenValid(jwtToken,userDetails)){
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(   //Create the Authentication object using  decoding the JWT token
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);            //sets authentication object in the SecurityContext using SecurityContextHolder.
                }
            }
            log.info("[JwtAccessTokenFilter:doFilterInternal] Completed");

            filterChain.doFilter(request,response);
        }catch (JwtValidationException jwtValidationException){
            log.error("[JwtAccessTokenFilter:doFilterInternal] Exception due to :{}",jwtValidationException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,jwtValidationException.getMessage());
        }
    }
}
