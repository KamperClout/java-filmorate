package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.ArrayList;
import java.util.List;

@Repository("MpaDbStorage")
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public MPA getRatingMpaById(int ratingId) {
        String sqlQuery = "SELECT * FROM mpa WHERE rating_id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, ratingId);
        if (srs.next()) {
            return new MPA(ratingId, srs.getString("rating_name"));
        }
        return null;
    }

    public List<MPA> getRatingsMpa() {
        List<MPA> ratingsMpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM mpa";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()) {
            ratingsMpa.add(new MPA(srs.getInt("rating_id"), srs.getString("rating_name")));
        }
        return ratingsMpa;
    }
}
