package com.example.rexcomputantistest.adapters;

import com.example.rexcomputantistest.model.user.User;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserControllerTestAdapter {

    @Autowired
    private TestRestTemplate testRestTemplate;


    public List<User> getAllUsers(int port) {

        ResponseEntity<List<User>> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        );

        Assertions.assertThat(getUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        return getUsersResponse.getBody();
    }

    public void deleteAllUsers(List<User> usersInApp, int port) {

        usersInApp.forEach(user -> {

            testRestTemplate.delete(
                    "http://localhost:" + port + "/users/{userId}", Map.of("userId", user.getId())
            );
        });
    }

    public User createUser(User newUserToCreate, int port) {

        ResponseEntity<User> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.POST,
                new HttpEntity<>(newUserToCreate),
                new ParameterizedTypeReference<User>() {
                }
        );

        Assertions.assertThat(getUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        return getUsersResponse.getBody();
    }
}
