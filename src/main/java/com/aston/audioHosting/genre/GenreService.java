package com.aston.audioHosting.genre;

import com.aston.audioHosting.exception.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@Service
public class GenreService {

    private final DBGenreStorage dbGenreStorage;

    @Autowired
    public GenreService(DBGenreStorage dbGenreStorage) {
        this.dbGenreStorage = dbGenreStorage;
    }

    public Genre getGerne(String name) {
        Genre genre = dbGenreStorage.getGenre(name);
        if (!Objects.equals(genre.getName(), name)) {
            throw new ObjectNotFoundException("Жанр с именем: " + name + " не был найден");
        }
        log.info("Жанр представлен");
        return genre;
    }

    public Collection<Genre> getAllGernes() {
        Collection<Genre> allGenres = dbGenreStorage.getAllGenres();
        if (allGenres.isEmpty()) {
            throw new ObjectNotFoundException("Жанры не найдены");
        }
        log.info("Список жанров представлен");
        return allGenres;
    }

}
