package com.example.rexcomputantistest.behavior.user.create;

import com.example.rexcomputantistest.configuration.ComponentTest;
import com.example.rexcomputantistest.model.user.dto.CreateUserDto;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@DisplayName("POST /users")
class CreateUserTest extends ComponentTest {

    int port = 8080;

    @Autowired
    CreateUserTest__handler handler;

    @BeforeEach
    void setUp() {
        handler.givenNoUsersInDb(port);
    }

    @DisplayName("Когда передаём пустой запрос на создание пользователя - должен вернуть ошибку")
    @Test
    void whenCallCreateUser__andEmptyBodyIsPassed__thenShouldReturnError() {
        ResponseEntity responseEntity = handler.whenCallCreateUserWithEmptyBody(port);
        handler.thenShouldReturnErrorResponse(responseEntity);
    }

    @DisplayName("Когда передаём некорректный запрос на создание пользователя - должен вернуть ошибку")
    @Test
    void whenCallCreateUser__andWrongBodyIsPassed__thenShouldReturnError() {
        ResponseEntity responseEntity = handler.whenCallCreateUserWithWrongBody(port);
        handler.thenShouldReturnErrorResponse(responseEntity);
    }

    @DisplayName("Когда передаём корректный запрос на создание пользователя - должен вернуть корректный ответ")
    @Test
    void whenCallCreateUser__andCorrectBodyIsPassed__thenShouldReturnOkResponseWithCorrectUser() {
        CreateUserDto createUserDto = handler.givenCorrectCreateUserBody();
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallCreateUserWithCorrectBody(port, createUserDto);
        handler.thenShouldReturnOkResponseWithCorrectUser(responseEntity, createUserDto);
    }

    @DisplayName("Когда передаём корректный запрос на создание пользователя - должен сохраниться корректный пользователь")
    @Test
    void whenCallCreateUser__andCorrectBodyIsPassed__thenShouldSaveUserInDb() {
        CreateUserDto createUserDto = handler.givenCorrectCreateUserBody();
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallCreateUserWithCorrectBody(port, createUserDto);
        handler.thenShouldSaveUserInDb(port, responseEntity, createUserDto);
    }
}