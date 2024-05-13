package com.aston.audioHosting.genre;

import java.util.Collection;

public interface GenreStorage {

    Genre getGenre(String name);

    Collection<Genre> getAllGenres();

}
