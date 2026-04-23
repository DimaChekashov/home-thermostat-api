package com.example.home_thermostat_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;
import com.example.home_thermostat_api.enums.Roles;
import com.example.home_thermostat_api.exception.AppException;
import com.example.home_thermostat_api.model.Role;
import com.example.home_thermostat_api.model.Token;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.RoleRepository;
import com.example.home_thermostat_api.repository.TokenRepository;
import com.example.home_thermostat_api.repository.UserRepository;
import com.example.home_thermostat_api.security.JwtTokenProviderImpl;
import com.example.home_thermostat_api.util.CookieUtil;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtTokenProviderImpl jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new AppException(HttpStatus.CONFLICT,
                    "Username '" + registerRequest.username() + "' already exists");
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new AppException(HttpStatus.CONFLICT,
                    "Email '" + registerRequest.email() + "' already exists");
        }

        User user = new User();

        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));

        Role roleUser = roleRepository.findByName(Roles.USER.name()).orElseThrow();
        user.setRole(roleUser);

        user = userRepository.save(user);

        Token accessToken = jwtUtil.generateAccessToken(Map.of("role", user.getRole().getAuthority()), user);
        Token refreshToken = jwtUtil.generateRefreshToken(user);

        HttpHeaders responseHeaders = new HttpHeaders();

        tokenRepository.saveAll(List.of(accessToken, refreshToken));
        addAccessTokenCookie(responseHeaders, accessToken);
        addRefreshTokenCookie(responseHeaders, refreshToken);

        return ResponseEntity.ok(RegisterResponse.ok());
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password()));

        String username = loginRequest.username();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        Token accessToken = jwtUtil.generateAccessToken(Map.of("role", user.getRole().getAuthority()), user);
        Token refreshToken = jwtUtil.generateRefreshToken(user);

        HttpHeaders responseHeaders = new HttpHeaders();

        revokeAllTokenOfUser(user);

        tokenRepository.saveAll(List.of(accessToken, refreshToken));
        addAccessTokenCookie(responseHeaders, accessToken);
        addRefreshTokenCookie(responseHeaders, refreshToken);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getName(), null);
        return ResponseEntity.ok()
                .headers(responseHeaders).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> refresh(String refreshToken) {
        boolean refreshTokenValid = jwtUtil.validateToken(refreshToken);

        if (!refreshTokenValid) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Refresh token is invalid");
        }

        String username = jwtUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        Token newAccessToken = jwtUtil.generateAccessToken(Map.of("role", user.getRole().getAuthority()), user);

        HttpHeaders responseHeaders = new HttpHeaders();
        addAccessTokenCookie(responseHeaders, newAccessToken);

        LoginResponse loginResponse = new LoginResponse(true, user.getRole().getName(), null);

        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken) {
        SecurityContextHolder.clearContext();

        String username = jwtUtil.getUsernameFromToken(accessToken);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, "User not found"));

        revokeAllTokenOfUser(user);

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString());

        LoginResponse loginResponse = new LoginResponse(false, null, null);

        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }

    private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createAccessTokenCookie(token.getValue()).toString());
    }

    private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(token.getValue()).toString());
    }

    private void revokeAllTokenOfUser(User user) {
        Set<Token> tokens = user.getTokens();

        tokens.forEach(token -> {
            if (token.getExpiryDate().isBefore(LocalDateTime.now()))
                tokenRepository.delete(token);
            else if (!token.isDisabled()) {
                token.setDisabled(true);
                tokenRepository.save(token);
            }
        });
    }
}
