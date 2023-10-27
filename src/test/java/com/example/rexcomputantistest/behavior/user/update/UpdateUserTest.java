package com.example.rexcomputantistest.behavior.user.update;

import com.example.rexcomputantistest.configuration.ComponentTest;
import com.example.rexcomputantistest.model.user.User;
import com.example.rexcomputantistest.model.user.dto.UpdateUserDto;
import com.example.rexcomputantistest.model.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@DisplayName("PATCH /users")
class UpdateUserTest extends ComponentTest {

    int port = 8080;

    @Autowired
    UpdateUserTest__handler handler;

    @BeforeEach
    void setUp() {
        handler.givenNoUsersInDb(port);
    }

    @DisplayName("Когда приложение не содержит данные о пользователях - должен вернуть 404")
    @Test
    void givenNoUsersInDB__thenShouldReturnNotFound() {
        handler.givenNoUsersInDb(port);
        ResponseEntity<UserResponseDto> usersFromRequest = handler.whenCallUpdateUserWithWrongId(port);
        handler.thenShouldReturnNotFoundErrorResponse(usersFromRequest);
    }

    @DisplayName("Когда приложение содержит данные о пользователе - и мы вызываем метод с id несуществующего пользователя - должен вернуть 404")
    @Test
    void whenCallGetUserWithWrongId__thenShouldReturnNotFound() {
        handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> usersFromRequest = handler.whenCallUpdateUserWithWrongId(port);
        handler.thenShouldReturnNotFoundErrorResponse(usersFromRequest);
    }

    @DisplayName("Когда передаём пустой запрос на обновление пользователя - должен вернуть ок")
    @Test
    void whenCallUpdateUser__andEmptyBodyIsPassed__thenShouldReturnOk() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallUpdateUserWithEmptyBody(port, userInDb);
        handler.thenShouldReturnOkResponse(responseEntity);
    }

    @DisplayName("Когда передаём пустой запрос на обновление пользователя - пользователь в БД должен остаться не изменённым")
    @Test
    void whenCallUpdateUser__andEmptyBodyIsPassed__thenShouldNotChangeUserInDb() {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallUpdateUserWithEmptyBody(port, userInDb);
        handler.thenShouldNotChangeUserInDb(port, responseEntity, userInDb);
    }

    @DisplayName("Когда передаём корректный запрос на обновление пользователя - должен вернуть корректный ответ")
    @ParameterizedTest(name = "{1}")
    @ArgumentsSource(UpdateUserTest__handler.CorrectDataForUpdateUserArgumentsProvider.class)
    void whenCallCreateUser__andCorrectBodyIsPassed__thenShouldReturnOkResponseWithCorrectUser(UpdateUserDto updateUserDto, String displayName) {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallUpdateUserWithCorrectBody(port, updateUserDto, userInDb);
        handler.thenShouldReturnOkResponseWithCorrectUser(responseEntity, updateUserDto, userInDb);
    }

    @DisplayName("Когда передаём корректный запрос на обновление пользователя - должен сохраниться корректный пользователь")
    @ParameterizedTest(name = "{1}")
    @ArgumentsSource(UpdateUserTest__handler.CorrectDataForUpdateUserArgumentsProvider.class)
    void whenCallCreateUser__andCorrectBodyIsPassed__thenShouldSaveUserInDb(UpdateUserDto updateUserDto, String displayName) {
        User userInDb = handler.givenOneUserInDb(port);
        ResponseEntity<UserResponseDto> responseEntity = handler.whenCallUpdateUserWithCorrectBody(port, updateUserDto, userInDb);
        handler.thenShouldSaveUserInDb(port, responseEntity, updateUserDto, userInDb);
    }
}