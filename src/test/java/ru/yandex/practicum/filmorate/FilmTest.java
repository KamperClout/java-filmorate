package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createWithEmptyName() {
        Film film = Film.builder()
                .name(" ")
                .description("Interesting")
                .releaseDate(LocalDate.now())
                .duration(180)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createWithDescriptionOver() {
        String description = "**".repeat(200);
        Film film = Film.builder()
                .name("13ReasonsWhy")
                .description(description)
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createWithDurationLow() {
        Film film = Film.builder()
                .name("13ReasonsWhy")
                .description("Interesting")
                .releaseDate(LocalDate.now().minusYears(20))
                .duration(-200)
                .build();
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }
}