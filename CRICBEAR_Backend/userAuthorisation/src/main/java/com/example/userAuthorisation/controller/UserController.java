package com.example.userAuthorisation.controller;

import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})
public class UserController {

    @Autowired
    private UserService userService;

    // Get all users
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(users);
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        boolean isAuthenticated = userService.authenticateUser(username, password);
        if (isAuthenticated) {
            User user1 = userService.getByUsername(username);
            if (user1 != null) {
                return ResponseEntity.ok(user1);
            } else {
                return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
    }

    // Get user role by username
    @PostMapping("/getRole")
    public ResponseEntity<Role> getUserRole(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        Role role = userService.getUserRole(username);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }

    // Set user role
    @PutMapping("/setUserRole/{uid}")
    public ResponseEntity<String> setUserRole(@PathVariable Integer uid, @RequestBody Map<String,Role> roleMap) {
        boolean isUpdated = userService.setUserRole(uid, roleMap.get("role"));
        if (isUpdated) {
            return new ResponseEntity<>("Role updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User or role not found", HttpStatus.NOT_FOUND);
        }
    }

    // Retrieve a user by the uid
    @GetMapping("/{uid}")
    public ResponseEntity<User> getUser(@PathVariable int uid) {
        User user = userService.getByUid(uid);

        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }
    }
}
