package com.example.userAuthorisation.service;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_UserNotExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userRepository.findByUsername("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    public void testRegisterUser_UserExists() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNull(registeredUser);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    public void testAuthenticateUser_Success() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedpassword")).thenReturn(true);

        boolean authenticated = userService.authenticateUser("testuser", "password");

        assertTrue(authenticated);
    }

    @Test
    public void testAuthenticateUser_Failure() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(passwordEncoder.matches("wrongpassword", "encodedpassword")).thenReturn(false);

        boolean authenticated = userService.authenticateUser("testuser", "wrongpassword");

        assertFalse(authenticated);
    }

    @Test
    public void testGetUserRole() {
        User user = new User();
        user.setUsername("testuser");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        Role role = userService.getUserRole("testuser");

        assertNotNull(role);
        assertEquals(Role.ADMIN, role);
    }

    @Test
    public void testSetUserRole() {
        User user = new User();
        user.setUid(1);
        user.setRole(Role.COACH);

        when(userRepository.findByUid(1)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        Boolean result = userService.setUserRole(1, Role.ADMIN);

        assertTrue(result);
        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    public void testGetByUid() {
        User user = new User();
        user.setUid(1);

        when(userRepository.findByUid(1)).thenReturn(user);

        User result = userService.getByUid(1);

        assertNotNull(result);
        assertEquals(1, result.getUid());
    }

    @Test
    public void testGetByUsername() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User result = userService.getByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }
}
