package com.aston.audioHosting.songs;

import com.aston.audioHosting.genre.DBGenreStorage;
import com.aston.audioHosting.genre.Genre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class DBSongStorage implements SongStorage{

    private final JdbcTemplate jdbcTemplate;
    private final DBGenreStorage dbGenreStorage;

    @Autowired
    public DBSongStorage(JdbcTemplate jdbcTemplate, DBGenreStorage dbGenreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.dbGenreStorage = dbGenreStorage;
    }

    @Override
    public Song createSong(Song song) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO songs (name, author, description, release_date, duration) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, song.getName());
            preparedStatement.setString(2, song.getAuthor());
            preparedStatement.setString(3, song.getDescription());
            preparedStatement.setDate(4, Date.valueOf(song.getReleaseDate()));
            preparedStatement.setLong(5, song.getDuration());
            return preparedStatement;
        }, keyHolder);
        song.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        String sqlGerne = "INSERT INTO song_genre (song_id, genre_id) VALUES (?, ?)";
        if (song.getGenres() != null) {
            song.getGenres().stream()
                    .map(g -> jdbcTemplate.update(sqlGerne, song.getId(), g.getId()))
                    .collect(Collectors.toSet());
            song.setGenres(findGenres(song.getId()));
        }
        return song;
    }


    @Override
    public Song getSong(String name) {
        String sql = "SELECT * FROM songs WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, this::makeSong, name);
    }

    @Override
    public Collection<Song> getAllSongs() {
        String sql = "SELECT * FROM songs";
        return jdbcTemplate.query(sql, this::makeSong);
    }

    @Override
    public Song updateSong(Song song) {
        String sql = "UPDATE songs SET name = ?, author = ?, " +
                "description = ?, release_date = ?, duration = ? WHERE id = ?";
        if (song.getGenres() != null) {
            String deleteGenres = "DELETE FROM song_genre WHERE song_id = ?";
            String updateGenres = "INSERT INTO song_genre (song_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(deleteGenres, song.getId());
            for (Genre g : song.getGenres()) {
                String checkDuplicate = "SELECT * FROM song_genre WHERE song_id = ? AND genre_id = ?";
                SqlRowSet checkRows = jdbcTemplate.queryForRowSet(checkDuplicate, song.getId(), g.getId());
                if (!checkRows.next()) {
                    jdbcTemplate.update(updateGenres, song.getId(), g.getId());
                }
                song.setGenres(findGenres(song.getId()));
            }
        }
        jdbcTemplate.update(sql, song.getName(), song.getAuthor(), song.getDescription(),
                song.getReleaseDate(), song.getDuration(), song.getId());
        return song;

    }

    @Override
    public Song deleteSong(String name) {
        Song song = getSong(name);
        long id = song.getId();
        deleteGenre(id);
        String sql = "DELETE FROM songs WHERE name = ?";
        jdbcTemplate.update(sql, name);
        return song;
    }

    @Override
    public Song likeSong(long songId, long userId) {
        String sql = "INSERT INTO song_likes (song_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, songId, userId);
        return getSongByID(songId);
    }

    @Override
    public Song removeLike(long songId, long userId) {
        String sql = "DELETE FROM song_likes WHERE song_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, songId, userId);
        return getSongByID(songId);
    }

    @Override
    public List<Song> getSongsOfUser(long userId) {
        String sql = "SELECT * FROM song_likes LEFT JOIN songs ON song_likes.song_id = songs.id WHERE user_id = ?";
        return jdbcTemplate.query(sql, this::makeSong, userId);
    }

    private List<Genre> findGenres(long songId) {
        String genresSql = "SELECT genre.genre_id, name FROM genre " +
                "LEFT JOIN song_genre FG on genre.genre_id = FG.GENRE_ID WHERE song_id = ?";
        return jdbcTemplate.query(genresSql, dbGenreStorage::makeGenre, songId);
    }

    private Song makeSong(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("id");
        return new Song(id,
                resultSet.getString("name"),
                resultSet.getString("author"),
                resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(),
                resultSet.getLong("duration"),
                findGenres(id));
    }

    private void deleteGenre(Long id) {
        String genreSql = "DELETE FROM song_genre WHERE song_id = ?";
        jdbcTemplate.update(genreSql, id);
    }

    private Song getSongByID (long id) {
        String sql = "SELECT * FROM songs WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, this::makeSong, id);
    }

}
