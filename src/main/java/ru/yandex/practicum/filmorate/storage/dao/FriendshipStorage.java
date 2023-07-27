package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(int userID, int friendId);

    void removeFriend(int userID, int friendId);

    List<User> getFriends(int userId);

    List<User> getMutualFriends(int friend1, int friend2);
}
