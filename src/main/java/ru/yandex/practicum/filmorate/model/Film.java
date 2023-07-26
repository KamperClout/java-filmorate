package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class Film {
    private int id;
    @NonNull
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private int duration;
    private Set<Genre> genres;
    private MPA mpa;
    @JsonIgnore
    private final Set<Integer> likes;


    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void removeAllGenres() {
        genres.clear();
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("duration", duration);
        values.put("release_date", java.sql.Date.valueOf(releaseDate));
        values.put("rating_id", mpa.getId());
        return values;
    }
}
