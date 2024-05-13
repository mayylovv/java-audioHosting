DROP TABLE IF EXISTS users, songs, genre, song_genre, song_likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id int generated by default as identity primary key,
    email varchar(50) not null,
    login varchar(20) not null,
    name varchar(50),
    birthday date not null
    );

CREATE TABLE IF NOT EXISTS songs (
    id int generated by default as identity primary key,
    name varchar(100) not null,
    author varchar not null,
    description varchar(200) not null,
    release_date date not null,
    duration int not null
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id int PRIMARY KEY,
    name varchar(255) not null
    );

CREATE TABLE IF NOT EXISTS song_genre (
    song_id int not null,
    genre_id int not null,
    primary key (song_id, genre_id),
    CONSTRAINT fk_song_genre FOREIGN KEY (song_id) REFERENCES songs (id),
    CONSTRAINT fk_genre_id FOREIGN KEY (genre_id) REFERENCES genre (genre_id)
    );

CREATE TABLE IF NOT EXISTS song_likes (
    song_id int not null,
    user_id int not null,
    primary key (song_id, user_id),
    CONSTRAINT fk_song_like FOREIGN KEY (song_id) REFERENCES songs (id),
    CONSTRAINT fk_user_like FOREIGN KEY (user_id) REFERENCES users (id)
    );