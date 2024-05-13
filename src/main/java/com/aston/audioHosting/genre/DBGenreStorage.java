package com.aston.audioHosting.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;


@Repository
public class DBGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenre(String name) {
        String sql = "SELECT * FROM genre WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, this::makeGenre, name);

    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    public Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("genre_id"),
                resultSet.getString("name"));
    }


}
