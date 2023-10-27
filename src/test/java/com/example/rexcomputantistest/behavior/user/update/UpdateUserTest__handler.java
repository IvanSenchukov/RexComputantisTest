package com.example.rexcomputantistest.behavior.user.update;

import com.example.rexcomputantistest.adapters.UserControllerTestAdapter;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.UpdateUserDto;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import com.example.rexcomputantistest.testData.TestDataUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class UpdateUserTest__handler {

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

    public User givenOneUserInDb(int port) {
        User newUserToCreate = testDataUserService.getRandomUser();
        User createdUser = userControllerTestAdapter.createUser(newUserToCreate, port);
        return createdUser;
    }


    public ResponseEntity<UserResponseDto> whenCallUpdateUserWithEmptyBody(int port, User userInDb) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/"+ userInDb.getId(),
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnOkResponse(ResponseEntity responseEntity) {
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public ResponseEntity<UserResponseDto> whenCallUpdateUserWithCorrectBody(int port, UpdateUserDto updateUserDto, User userInDb) {

        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/" + userInDb.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(updateUserDto),
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnOkResponseWithCorrectUser(
            ResponseEntity<UserResponseDto> responseEntity,
            UpdateUserDto updateUserDto,
            User userInDb
    ) {

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto updatedUserFromResponse = responseEntity.getBody();

        Assertions.assertThat(updatedUserFromResponse.getId()).isEqualTo(userInDb.getId());

        if (updateUserDto.getName() != null) Assertions.assertThat(updatedUserFromResponse.getName()).isEqualTo(updateUserDto.getName());
        if (updateUserDto.getName() == null) Assertions.assertThat(updatedUserFromResponse.getName()).isEqualTo(userInDb.getName());
        if (updateUserDto.getDefaultCurrency() != null) Assertions.assertThat(updatedUserFromResponse.getDefaultCurrency()).isEqualTo(updateUserDto.getDefaultCurrency());
        if (updateUserDto.getDefaultCurrency() == null) Assertions.assertThat(updatedUserFromResponse.getDefaultCurrency()).isEqualTo(userInDb.getDefaultCurrency());
    }


    public void thenShouldSaveUserInDb(int port, ResponseEntity<UserResponseDto> responseEntity, UpdateUserDto updateUserDto, User userInDb) {
        List<User> usersInApp = userControllerTestAdapter.getAllUsers(port);

        Assertions.assertThat(usersInApp).hasSize(1);

        User updatedUserFromDb = usersInApp.get(0);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto updatedUserFromResponse = responseEntity.getBody();

        Assertions.assertThat(updatedUserFromResponse).isNotNull();

        if (updateUserDto.getName() != null) Assertions.assertThat(updatedUserFromDb.getName()).isEqualTo(updateUserDto.getName());
        if (updateUserDto.getName() == null) Assertions.assertThat(updatedUserFromDb.getName()).isEqualTo(userInDb.getName());
        if (updateUserDto.getDefaultCurrency() != null) Assertions.assertThat(updatedUserFromDb.getDefaultCurrency()).isEqualTo(updateUserDto.getDefaultCurrency());
        if (updateUserDto.getDefaultCurrency() == null) Assertions.assertThat(updatedUserFromDb.getDefaultCurrency()).isEqualTo(userInDb.getDefaultCurrency());
    }

    public ResponseEntity<UserResponseDto> whenCallUpdateUserWithWrongId(int port) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity httpEntity = new HttpEntity(new UpdateUserDto().setName("name").setName("defaultCurrency"),headers);

        ResponseEntity<UserResponseDto> getUsersResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/users/-1",
                HttpMethod.PATCH,
                httpEntity,
                new ParameterizedTypeReference<UserResponseDto>() {
                }
        );

        return getUsersResponse;
    }

    public void thenShouldReturnNotFoundErrorResponse(ResponseEntity<UserResponseDto> usersFromRequest) {
        Assertions.assertThat(usersFromRequest.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    public void thenShouldNotChangeUserInDb(int port, ResponseEntity<UserResponseDto> responseEntity, User userInDb) {
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<User> usersInApp = userControllerTestAdapter.getAllUsers(port);

        Assertions.assertThat(usersInApp).hasSize(1);

        User user = usersInApp.get(0);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        UserResponseDto updatedUserFromResponse = responseEntity.getBody();

        Assertions.assertThat(updatedUserFromResponse).isNotNull();

        Assertions.assertThat(user.getId()).isEqualTo(updatedUserFromResponse.getId());
        Assertions.assertThat(user.getName()).isEqualTo(userInDb.getName());
        Assertions.assertThat(user.getDefaultCurrency()).isEqualTo(userInDb.getDefaultCurrency());
    }


    public static class CorrectDataForUpdateUserArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(
                            new UpdateUserDto()
                                    .setName("newName"),
                            "Должно измениться только имя"
                    ),
                    Arguments.of(
                            new UpdateUserDto()
                                    .setDefaultCurrency("USD"),
                            "Должна измениться валюта по-умолчанию"
                    ),
                    Arguments.of(
                            new UpdateUserDto()
                                    .setName("newName")
                                    .setDefaultCurrency("USD"),
                            "Должно измениться и имя и валюта по-умолчанию"
                    ),
                    Arguments.of(
                            new UpdateUserDto(),
                            "Не должно измениться ничего"
                    )
            );
        }

    }
}
