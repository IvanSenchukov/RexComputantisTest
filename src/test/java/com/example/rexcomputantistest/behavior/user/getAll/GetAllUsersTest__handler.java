package com.example.rexcomputantistest.behavior.user.getAll;

import com.example.rexcomputantistest.adapters.UserControllerTestAdapter;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
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
import java.util.Objects;

@Component
public class GetAllUsersTest__handler {

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

    public ResponseEntity<List<UserResponseDto>> whenCallGetUsers(int port) {
        ResponseEntity<List<UserResponseDto>> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserResponseDto>>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnEmptyCollection(ResponseEntity<List<UserResponseDto>> usersFromRequest) {
        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(usersFromRequest.getBody()).hasSize(0);
    }

    public User givenOneUserInDb(int port) {
        User newUserToCreate = testDataUserService.getRandomUser();
        User createdUser = userControllerTestAdapter.createUser(newUserToCreate, port);
        return createdUser;
    }

    public void thenShouldReturnCollectionWithThatGivenUser(ResponseEntity<List<UserResponseDto>> usersFromRequest, User userInDb) {
        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<UserResponseDto> users = usersFromRequest.getBody();
        Assertions.assertThat(users).hasSize(1);

        UserResponseDto user = users.get(0);

        Assertions.assertThat(user.getId()).isEqualTo(userInDb.getId());
        Assertions.assertThat(user.getName()).isEqualTo(userInDb.getName());
        Assertions.assertThat(user.getDefaultCurrency()).isEqualTo(userInDb.getDefaultCurrency());

    }

    public void thenShouldReturnCollectionWithThatGivenUsers(
            ResponseEntity<List<UserResponseDto>> usersFromRequest,
            User userOneInDb,
            User userTwoInDb
    ) {

        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<UserResponseDto> users = usersFromRequest.getBody();
        Assertions.assertThat(users).hasSize(2);

        UserResponseDto userOneFromResponse = users.stream().filter(currentUser -> Objects.equals(currentUser.getId(), userOneInDb.getId())).findFirst().get();

        Assertions.assertThat(userOneFromResponse.getId()).isEqualTo(userOneInDb.getId());
        Assertions.assertThat(userOneFromResponse.getName()).isEqualTo(userOneInDb.getName());
        Assertions.assertThat(userOneFromResponse.getDefaultCurrency()).isEqualTo(userOneInDb.getDefaultCurrency());

        UserResponseDto userTwoFromResponse = users.stream().filter(currentUser -> Objects.equals(currentUser.getId(), userTwoInDb.getId())).findFirst().get();

        Assertions.assertThat(userTwoFromResponse.getId()).isEqualTo(userTwoInDb.getId());
        Assertions.assertThat(userTwoFromResponse.getName()).isEqualTo(userTwoInDb.getName());
        Assertions.assertThat(userTwoFromResponse.getDefaultCurrency()).isEqualTo(userTwoInDb.getDefaultCurrency());
    }
}
