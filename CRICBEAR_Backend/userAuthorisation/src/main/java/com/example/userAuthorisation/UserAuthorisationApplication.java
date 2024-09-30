package com.example.userAuthorisation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.userAuthorisation.model.User;
import com.example.userAuthorisation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@SpringBootApplication
public class UserAuthorisationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthorisationApplication.class, args);
	}

}


