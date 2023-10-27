package com.example.rexcomputantistest.behavior.user.getAll;

import com.example.rexcomputantistest.configuration.ComponentTest;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.List;

@DisplayName("GET /users")
class GetAllUsersTest extends ComponentTest {

    int port = 8080;

    @Autowired
    GetAllUsersTest__handler handler;

    @BeforeEach
    void setUp() {
        handler.givenNoUsersInDb(port);
    }

    @DisplayName("Когда приложение не содержит данные о пользователях - должен вернуть пустую коллекцию")
    @Test
    void givenNoUsersInDB__thenShouldReturnEmptyCollection() {
        handler.givenNoUsersInDb(port);
        ResponseEntity<List<UserResponseDto>> usersFromRequest = handler.whenCallGetUsers(port);
        handler.thenShouldReturnEmptyCollection(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные об одном пользователе - должен вернуть коллекцию с указанным пользователем")
    @Test
    void givenOneUserInDB__thenShouldReturnCollectionWithThatGivenUser() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<List<UserResponseDto>> usersFromRequest = handler.whenCallGetUsers(port);
        handler.thenShouldReturnCollectionWithThatGivenUser(usersFromRequest, userInDb);
    }

    @DisplayName("Когда приложение содержит данные о нескольких пользователях - должен вернуть коллекцию с указанными пользователями")
    @Test
    void givenTwoUsersInDB__thenShouldReturnCollectionWithThatGivenUser() {
        User userOneInDb = handler.givenOneUserInDb(port);
        User userTwoInDb = handler.givenOneUserInDb(port);
        ResponseEntity<List<UserResponseDto>> usersFromRequest = handler.whenCallGetUsers(port);
        handler.thenShouldReturnCollectionWithThatGivenUsers(usersFromRequest, userOneInDb, userTwoInDb);
    }
}