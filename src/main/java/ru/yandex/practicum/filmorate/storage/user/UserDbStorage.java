package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage, FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        log.info("Пользователь сохранен в таблице users");
        return get(userId);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users "
                + "SET name = ?, "
                + "login = ?, "
                + "email = ?, "
                + "birthday = ? "
                + "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (srs.next()) {
            users.add(mapRowToUser(srs));
        }
        return users;
    }

    @Override
    public User get(int id) {
        String sqlQuery = "SELECT * FROM users where id = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return mapRowToUser(srs);
        } else {
            throw new NotFoundException("User with ID=" + id + " not found!");
        }
    }


    public void addFriend(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id, status) "
                + "VALUES(?, ?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId, true);
    }

    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE friendship "
                + "WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    public List<User> getFriends(int userId) {
        List<User> friends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                + "WHERE users.id IN (SELECT friend_id from friendship "
                + "WHERE user_id = ?)";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (srs.next()) {
            friends.add(UserDbStorage.mapRowToUser(srs));
        }
        return friends;
    }

    public List<User> getMutualFriends(int friend1, int friend2) {
        List<User> mutualFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users "
                + "WHERE users.id IN (SELECT friend_id from friendship "
                + "WHERE user_id IN (?, ?) "
                + "AND friend_id NOT IN (?, ?))";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, friend1, friend2, friend1, friend2);
        while (srs.next()) {
            mutualFriends.add(UserDbStorage.mapRowToUser(srs));
        }
        return mutualFriends;
    }

    private static User mapRowToUser(SqlRowSet rowSet) {
        return User.builder()
                .id(rowSet.getInt("id"))
                .name(rowSet.getString("name"))
                .login(Objects.requireNonNull(rowSet.getString("login")))
                .email(Objects.requireNonNull(rowSet.getString("email")))
                .birthday(Objects.requireNonNull(rowSet.getDate("birthday")).toLocalDate())
                .build();
    }
}
