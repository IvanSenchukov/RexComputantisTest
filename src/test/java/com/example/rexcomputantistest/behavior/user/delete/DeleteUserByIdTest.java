package com.example.rexcomputantistest.behavior.user.delete;

import com.example.rexcomputantistest.configuration.ComponentTest;
import com.example.rexcomputantistest.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@DisplayName("DELETE /users/{id}")
class DeleteUserByIdTest extends ComponentTest {

    int port = 8080;

    @Autowired
    DeleteUserByIdTest__handler handler;

    @BeforeEach
    void setUp() {
        handler.givenNoUsersInDb(port);
    }

    @DisplayName("Когда приложение не содержит данные о пользователях - должен вернуть 200")
    @Test
    void givenNoUsersInDB__thenShouldReturnOk() {
        handler.givenNoUsersInDb(port);
        ResponseEntity<String> usersFromRequest = handler.whenCallDeleteUserWithWrongId(port);
        handler.thenShouldReturnOkResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id несуществующего пользователя - должен вернуть 200")
    @Test
    void whenCallGetUserWithWrongId__thenShouldReturnOk() {
        handler.givenOneUserInDb(port);
        ResponseEntity<String> usersFromRequest = handler.whenCallDeleteUserWithWrongId(port);
        handler.thenShouldReturnOkResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id этого пользователя - должен вернуть 200")
    @Test
    void whenCallDeleteUserWithCorrectId__thenShouldReturnOk() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<String> usersFromRequest = handler.whenCallGetUserWithId(port, userInDb);
        handler.thenShouldReturnOkResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id этого пользователя - должен удалить пользователя из БД")
    @Test
    void whenCallDeleteUserWithCorrectId__thenShouldDeleteUserFromDB() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<String> usersFromRequest = handler.whenCallGetUserWithId(port, userInDb);
        handler.thenShouldDeleteUserFromDB(port);
    }
}