package com.example.rexcomputantistest.behavior.user.getById;

import com.example.rexcomputantistest.configuration.ComponentTest;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@DisplayName("GET /users/{id}")
class GetUserByIdTest extends ComponentTest {

    int port = 8080;

    @Autowired
    GetUserByIdTest__handler handler;

    @BeforeEach
    void setUp() {
        handler.givenNoUsersInDb(port);
    }

    @DisplayName("Когда приложение не содержит данные о пользователях - должен вернуть 404")
    @Test
    void givenNoUsersInDB__thenShouldReturnNotFound() {
        handler.givenNoUsersInDb(port);
        ResponseEntity<UserResponseDto> usersFromRequest = handler.whenCallGetUserWithWrongId(port);
        handler.thenShouldReturnNotFoundErrorResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id несуществующего пользователя - должен вернуть 404")
    @Test
    void whenCallGetUserWithWrongId__thenShouldReturnNotFound() {
        handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> usersFromRequest = handler.whenCallGetUserWithWrongId(port);
        handler.thenShouldReturnNotFoundErrorResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id этого пользователя - должен вернуть 200 с телом пользователя")
    @Test
    void whenCallGetUsersWithCorrectId__thenShouldReturnUser() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> usersFromRequest = handler.whenCallGetUserWithId(port, userInDb);
        handler.thenShouldReturnThatGivenUser(usersFromRequest, userInDb);
    }
}