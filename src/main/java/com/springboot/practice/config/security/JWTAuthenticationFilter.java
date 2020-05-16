package com.springboot.practice.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.unit.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.springboot.practice.config.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            UserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    userDTO.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(userDTO.getRole().name()))));
        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {
                throw new com.springboot.practice.exceptions.auth.BadCredentialsException("Invalid login or password");
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) {
        User user = (User) auth.getPrincipal();
        UserDTO userDto = userService.getUserByUsername(user.getUsername());
        userDto.setPassword(null);

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(USER_ID_PARAM, userDto.getId())
                .withClaim(USER_ROLE_PARAM, userDto.getRole().name())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER, TOKEN_PREFIX + token);
        response.setContentType("application/json");
    }
}
