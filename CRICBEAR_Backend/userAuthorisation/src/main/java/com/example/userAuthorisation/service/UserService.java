package com.example.userAuthorisation.service;
import com.example.userAuthorisation.model.Role;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user if the username is not already taken
    public User registerUser(User user) {
        if(userRepository.findByUsername(user.getUsername()) != null) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Retrieve all users from the repository
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Authenticate a user based on username and password
    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    // Get the role of a user by their username
    public Role getUserRole(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getRole();
        }
        return null;
    }

    // Set the role for a user identified by their UID
    public Boolean setUserRole(int uid, Role role) {
        User user = userRepository.findByUid(uid);
        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    // Get a user by the primary key
    public User getByUid(int uid) {
        return userRepository.findByUid(uid);
    }

    // Get a user by their username
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}