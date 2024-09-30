package com.example.userAuthorisation.repository;

import com.example.userAuthorisation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Integer> {

        //Find a user by the username
        User findByUsername(String username);

        //Find a user by the primary key
        User findByUid(Integer uid);
}

