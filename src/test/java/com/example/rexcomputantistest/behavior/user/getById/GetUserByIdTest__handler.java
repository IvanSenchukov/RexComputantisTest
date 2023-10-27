package com.example.rexcomputantistest.behavior.user.getById;

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

@Component
public class GetUserByIdTest__handler {

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

    public ResponseEntity<UserResponseDto> whenCallGetUserWithWrongId(int port) {
        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/-1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnNotFoundErrorResponse(ResponseEntity<UserResponseDto> usersFromRequest) {
        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<UserResponseDto> whenCallGetUserWithId(int port, User userInDb) {
        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/" + userInDb.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnThatGivenUser(ResponseEntity<UserResponseDto> usersFromRequestEnvelope, User userInDb) {

        Assertions.assertThat(usersFromRequestEnvelope.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto userFromRequest = usersFromRequestEnvelope.getBody();

        Assertions.assertThat(userFromRequest.getId()).isEqualTo(userInDb.getId());
        Assertions.assertThat(userFromRequest.getName()).isEqualTo(userInDb.getName());
        Assertions.assertThat(userFromRequest.getDefaultCurrency()).isEqualTo(userInDb.getDefaultCurrency());
    }
}
