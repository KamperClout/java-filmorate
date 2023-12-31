DROP TABLE IF EXISTS film_genres;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS mpa;

CREATE TABLE IF NOT EXISTS genres(
    genre_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa(
    rating_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name VARCHAR(40) NOT NULL
);

CREATE TABlE IF NOT EXISTS films(
    film_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(200) NOT NULL,
    duration INT NOT NULL,
    release_date DATE NOT NULL,
    rating_id INT REFERENCES mpa(rating_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS users(
    id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200),
    login VARCHAR(200) NOT NULL,
    email VARCHAR(200) NOT NULL,
    birthday DATE
);

CREATE TABLE IF NOT EXISTS film_genres(
    film_id INT NOT NULL REFERENCES films (film_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    genre_id INT NOT NULL REFERENCES genres (genre_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS likes(
    film_id INT NOT NULL REFERENCES films (film_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    user_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship(
    user_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    friend_id INT NOT NULL REFERENCES users (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    status BOOLEAN NOT NULL
);