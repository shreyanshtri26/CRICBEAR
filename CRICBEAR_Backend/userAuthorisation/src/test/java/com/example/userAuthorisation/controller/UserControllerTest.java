package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty());
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testRegisterUser_Failure() throws Exception {
        when(userService.registerUser(any(User.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");

        when(userService.authenticateUser("testuser", "password")).thenReturn(true);
        when(userService.getByUsername("testuser")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testLoginUser_Failure() throws Exception {
        when(userService.authenticateUser("testuser", "password")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"password\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetUserRole() throws Exception {
        Role role = Role.ADMIN;
        when(userService.getUserRole("testuser")).thenReturn(role);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/getRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("ADMIN"));
    }

    @Test
    public void testGetUserRole_NotFound() throws Exception {
        when(userService.getUserRole("testuser")).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/getRole")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSetUserRole_Success() throws Exception {
        Map<String, Role> roleMap = new HashMap<>();
        roleMap.put("role", Role.ADMIN);

        when(userService.setUserRole(1, Role.ADMIN)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/setUserRole/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ADMIN\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Role updated successfully"));
    }

    @Test
    public void testSetUserRole_NotFound() throws Exception {
        Map<String, Role> roleMap = new HashMap<>();
        roleMap.put("role", Role.ADMIN);

        when(userService.setUserRole(1, Role.ADMIN)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.put("/user/setUserRole/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"role\":\"ADMIN\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUser() throws Exception {
        User user = new User();
        user.setUid(1);

        when(userService.getByUid(1)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.uid").value(1));
    }

    @Test
    public void testGetUser_NotFound() throws Exception {
        when(userService.getByUid(1)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
