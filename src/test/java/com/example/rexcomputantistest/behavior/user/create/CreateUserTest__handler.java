package com.example.rexcomputantistest.behavior.user.create;

import com.example.rexcomputantistest.adapters.UserControllerTestAdapter;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.CreateUserDto;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import com.example.rexcomputantistest.testData.TestDataUserService;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateUserTest__handler {

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

    public ResponseEntity whenCallCreateUserWithEmptyBody(int port) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<String>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnErrorResponse(ResponseEntity responseEntity) {
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity whenCallCreateUserWithWrongBody(int port) {

        CreateUserDto wrongCreateUserDto = new CreateUserDto();
        wrongCreateUserDto.setName(null);
        wrongCreateUserDto.setDefaultCurrency("USD");

        ResponseEntity<String> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.POST,
                new HttpEntity<>(wrongCreateUserDto),
                new ParameterizedTypeReference<String>() {
                }
        );

        return getUsersResponse;
    }

    public ResponseEntity<UserResponseDto> whenCallCreateUserWithCorrectBody(int port, CreateUserDto correctCreateUserDto) {

        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users",
                HttpMethod.POST,
                new HttpEntity<>(correctCreateUserDto),
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnOkResponseWithCorrectUser(ResponseEntity<UserResponseDto> responseEntity, CreateUserDto createUserDto) {

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto createdUserFromResponse = responseEntity.getBody();

        Assertions.assertThat(createdUserFromResponse.getId()).isNotNull();
        Assertions.assertThat(createdUserFromResponse.getName()).isEqualTo(createUserDto.getName());
        Assertions.assertThat(createdUserFromResponse.getDefaultCurrency()).isEqualTo(createUserDto.getDefaultCurrency());
    }

    public CreateUserDto givenCorrectCreateUserBody() {

        CreateUserDto correctCreateUserDto = new CreateUserDto();
        correctCreateUserDto.setName("correctUserName");
        correctCreateUserDto.setDefaultCurrency("USD");

        return correctCreateUserDto;
    }

    public void thenShouldSaveUserInDb(int port, ResponseEntity<UserResponseDto> responseEntity, CreateUserDto createUserDto) {
        List<User> usersInApp = userControllerTestAdapter.getAllUsers(port);

        Assertions.assertThat(usersInApp).hasSize(1);

        User userInDb = usersInApp.get(0);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto createdUserFromResponse = responseEntity.getBody();

        Assertions.assertThat(createdUserFromResponse).isNotNull();

        Assertions.assertThat(userInDb.getId()).isEqualTo(createdUserFromResponse.getId());
        Assertions.assertThat(userInDb.getName()).isEqualTo(createUserDto.getName());
        Assertions.assertThat(userInDb.getDefaultCurrency()).isEqualTo(createUserDto.getDefaultCurrency());
    }
}
