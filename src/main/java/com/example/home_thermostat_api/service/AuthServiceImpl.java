package com.example.home_thermostat_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.home_thermostat_api.dto.LoginRequest;
import com.example.home_thermostat_api.dto.LoginResponse;
import com.example.home_thermostat_api.dto.RegisterRequest;
import com.example.home_thermostat_api.dto.RegisterResponse;
import com.example.home_thermostat_api.dto.TokenRefreshRequest;
import com.example.home_thermostat_api.exception.AppException;
import com.example.home_thermostat_api.model.Token;
import com.example.home_thermostat_api.model.User;
import com.example.home_thermostat_api.repository.TokenRepository;
import com.example.home_thermostat_api.repository.UserRepository;
import com.example.home_thermostat_api.security.JwtUtil;
import com.example.home_thermostat_api.util.CookieUtil;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest registerRequest) {

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
    ResponseEntity<LoginResponse> refresh(TokenRefreshRequest request) {

    }

    @Override
    ResponseEntity<LoginResponse> logout(TokenRefreshRequest request) {

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

// @Service
// public class AuthService {
// @Autowired
// private UserRepository userRepository;

// @Autowired
// private PasswordEncoder passwordEncoder;

// @Autowired
// private JwtUtil jwtUtil;

// public String register(String name, String email, String password) {
// if (userRepository.existsByName(name)) {
// throw new RuntimeException("Login '" + name + "' is already exist");
// }

// if (userRepository.existsByEmail(email)) {
// throw new RuntimeException("Email '" + email + "' is already use");
// }

// User user = new User();
// user.setName(name);
// user.setEmail(email);
// user.setPassword(passwordEncoder.encode(password));
// user.setRole("ROLE_USER");

// User savedUser = userRepository.save(user);
// System.out.println("User saved with ID: " + savedUser.getId());

// return jwtUtil.generateToken(savedUser.getName());
// }

// public String login(String name) {
// return jwtUtil.generateToken(name);
// }

// }