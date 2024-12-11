package com.event.management.controller;

import com.event.management.dto.UserRegistrationDto;
import com.event.management.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(Authentication authentication, HttpServletResponse response){
        return ResponseEntity.ok(authService.getJwtTokensAfterAuthentication(authentication,response));
    }

    @PreAuthorize("hasAuthority('SCOPE_REFRESH_TOKEN')")      //use to get accsess token using refresh token
    @PostMapping ("/refresh-token")
    public ResponseEntity<?> getAccessToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader){
        log.info("authorizationHeader ", authorizationHeader);
        return ResponseEntity.ok(authService.getAccessTokenUsingRefreshToken(authorizationHeader));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDto,
                                          BindingResult bindingResult, HttpServletResponse httpServletResponse) {
        log.info("[AuthController:registerUser] Signup Process Started for user:{}", userRegistrationDto.userName());

        if (bindingResult.hasErrors()) {
            List<String> errorMessage = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            log.error("[AuthController:registerUser] Errors in user:{}", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }

        try {
            return ResponseEntity.ok(authService.registerUser(userRegistrationDto, httpServletResponse));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            log.error("[AuthController:registerUser] Unexpected error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }






}

//We can get the access token using refresh token. after we logout using the that refresh token. we can not get again access token using that same refresh token