package com.example.rexcomputantistest.behavior.user.delete;

import com.example.rexcomputantistest.adapters.UserControllerTestAdapter;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.testData.TestDataUserService;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeleteUserByIdTest__handler {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    UserControllerTestAdapter userControllerTestAdapter;

    @Autowired
    TestDataUserService testDataUserService;

    public void givenNoUsersInDb(int port) {

        List<User> usersInAppFirstTime = userControllerTestAdapter.getAllUsers(port);
        userControllerTestAdapter.deleteAllUsers(usersInAppFirstTime, port);
        List<User> usersInAppAfterDeletion = userControllerTestAdapter.getAllUsers(port);
        Assertions.assertThat(usersInAppAfterDeletion).hasSize(0);
    }

    public User givenOneUserInDb(int port) {
        User newUserToCreate = testDataUserService.getRandomUser();
        User createdUser = userControllerTestAdapter.createUser(newUserToCreate, port);
        return createdUser;
    }

    public ResponseEntity<String> whenCallDeleteUserWithWrongId(int port) {
        ResponseEntity<String> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/-1",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnOkResponse(ResponseEntity<String> usersFromRequest) {
        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public ResponseEntity<String> whenCallGetUserWithId(int port, User userInDb) {
        ResponseEntity<String> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/" + userInDb.getId(),
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<String>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldDeleteUserFromDB(int port) {
        List<User> usersInApp = userControllerTestAdapter.getAllUsers(port);
        Assertions.assertThat(usersInApp).hasSize(0);
    }
}
