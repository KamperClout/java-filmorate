package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createWithFalseEmail() {
        User user = User.builder()
                .id(1)
                .name("Pavel")
                .email("kamperincyandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .login("kamper")
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createWithEmptyLogin() {
        User user = User.builder()
                .id(1)
                .name("Pavel")
                .email("kamper.inc@yandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .login(" ")
                .build();
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }


    @Test
    void updateUserToEmptyLogin_shouldShowErrorMessage() {
        User user = User.builder()
                .id(1)
                .name("Pavel")
                .email("kamper.inc@yandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .login("kamper")
                .build();
        restTemplate.postForEntity("/users", user, User.class);
        User user2 = User.builder()
                .id(1)
                .name("Pavel")
                .email("kamperincyandex.ru")
                .birthday(LocalDate.of(2003, 10, 17))
                .login(" ")
                .build();
        HttpEntity<User> entity = new HttpEntity<>(user2);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);

        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

}