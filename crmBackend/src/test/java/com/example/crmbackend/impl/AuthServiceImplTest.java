package com.example.crmbackend.impl;

import com.example.crmbackend.dto.request.LoginRequest;
import com.example.crmbackend.dto.response.AuthResponse;
import com.example.crmbackend.entity.Role;
import com.example.crmbackend.entity.User;
import com.example.crmbackend.exception.UnauthorizedException;
import com.example.crmbackend.repository.UserRepository;
import com.example.crmbackend.security.JwtService;
import com.example.crmbackend.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@test.com");
        mockUser.setPasswordHash("hashedPass");
        mockUser.setRole(Role.ADMIN);
        mockUser.setActif(true);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password");
    }

    @Test
    void login_Success() {
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("password", "hashedPass")).thenReturn(true);
        when(jwtService.generateToken("test@test.com", "ADMIN", 1L)).thenReturn("mocked-token");

        AuthResponse res = authService.login(loginRequest);

        assertNotNull(res);
        assertEquals("mocked-token", res.getToken());
        assertEquals("test@test.com", res.getEmail());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WrongPassword_ThrowsException() {
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("password", "hashedPass")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_InactiveAccount_ThrowsException() {
        mockUser.setActif(false);
        when(userRepo.findByEmail("test@test.com")).thenReturn(Optional.of(mockUser));
        when(encoder.matches("password", "hashedPass")).thenReturn(true);

        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
    }
}
