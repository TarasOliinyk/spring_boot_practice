package com.springboot.practice.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.practice.dto.UserDTO;
import com.springboot.practice.model.Permission;
import com.springboot.practice.model.Role;
import com.springboot.practice.repository.UserRepository;
import com.springboot.practice.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.persistence.EntityNotFoundException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.springboot.practice.config.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService,
                                   UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            UserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            com.springboot.practice.model.User user = userRepository.findOneByUsername(userDTO.getUsername())
                    .orElseThrow(EntityNotFoundException::new);
            List<SimpleGrantedAuthority> permissions = user.getRoles().stream().map(Role::getPermissions)
                    .flatMap(Collection::stream).map(Permission::getName).map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userDTO.getUsername(),
                    userDTO.getPassword(),
                    permissions));
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
        UserDTO userDTO = userService.getUserByUsername(user.getUsername());
        userDTO.setPassword(null);

        com.springboot.practice.model.User userEntity = userRepository.findOneByUsername(userDTO.getUsername())
                .orElseThrow(EntityNotFoundException::new);
        List<String> permissions = userEntity.getRoles().stream().map(Role::getPermissions).flatMap(Collection::stream)
                .map(Permission::getName).collect(Collectors.toList());

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim(USER_ID_PARAM, userDTO.getId())
                .withClaim(USER_ROLE_PARAM, permissions)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        response.addHeader(HEADER, TOKEN_PREFIX + token);
        response.setContentType("application/json");
    }
}
