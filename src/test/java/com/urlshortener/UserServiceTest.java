package com.urlshortener;

import com.urlshortener.dto.LoginRegistrationRequest;
import com.urlshortener.exception.InvalidCredentialException;
import com.urlshortener.model.User;
import com.urlshortener.repository.UserDetailRepository;
import com.urlshortener.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDetailRepository userDetailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;


    @Test
    void registerUser_newUser_returnSuccess() {
        LoginRegistrationRequest request = new LoginRegistrationRequest();
        request.setUserName("usha@test.com");
        request.setPassword("pass1234");
        when(userDetailRepository.existsByUserName("usha@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass1234")).thenReturn("hashedPassword");
        String result = userService.registerUser(request);

        assertEquals("User created successfully", result);


    }

    @Test
    void registerUser_duplicateUser_returnsAlreadyExists() {
        LoginRegistrationRequest request = new LoginRegistrationRequest();
        request.setUserName("usha@test.com");
        request.setPassword("pass1234");
        when(userDetailRepository.existsByUserName("usha@test.com")).thenReturn(true);
        String result = userService.registerUser(request);

        assertEquals("User Already Exists, Please Login!!", result);

    }

    @Test
    void loginUser_returnSuccess() {
        LoginRegistrationRequest request = new LoginRegistrationRequest();
        request.setUserName("usha@test.com");
        request.setPassword("pass1234");

        User user = new User();
        user.setUserName("usha@test.com");
        user.setPassword("pass1234");
        when(userDetailRepository.findByUserName("usha@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass1234", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken("usha@test.com")).thenReturn("fake-token-123");
        String result = userService.loginUser(request);

        assertEquals("fake-token-123", result);


    }

    @Test
    void loginUser_failureCase() {
        LoginRegistrationRequest request = new LoginRegistrationRequest();
        request.setUserName("usha@test.com");
        request.setPassword("pass1234");

        User user = new User();
        user.setUserName("usha@test.com");
        user.setPassword("pass1234");
        when(userDetailRepository.findByUserName("usha@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass1234", user.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialException.class, () -> userService.loginUser(request));

    }

    @Test
    void loginUser_notExists() {
        LoginRegistrationRequest request = new LoginRegistrationRequest();
        request.setUserName("usha@test.com");
        request.setPassword("pass1234");

        when(userDetailRepository.findByUserName("usha@test.com")).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialException.class, () -> userService.loginUser(request));

    }
}