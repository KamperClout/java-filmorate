package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void delete(User user) {
        userStorage.delete(user);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User get(int id) {
        return userStorage.get(id);
    }

    public void addFriend(int idUser, int idFriend) {
        User user = get(idUser);
        User friend = get(idFriend);

        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void removeFriend(int idUser, int idFriend) {
        User user = get(idUser);
        User friend = get(idFriend);

        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public List<User> getMutualFriends(int idUser1, int idUser2) {
        User user1 = get(idUser1);
        User user2 = get(idUser2);

        Set<Integer> mutualFriends = new HashSet<>(user1.getFriends());
        mutualFriends.retainAll(user2.getFriends());

        return mutualFriends.stream().map(u -> get(u)).collect(Collectors.toList());
    }

    public List<User> getFriends(int id) {
        User user = get(id);
        return user.getFriends().stream().map(f -> get(f)).collect(Collectors.toList());
    }
}
