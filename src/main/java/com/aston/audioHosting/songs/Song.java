package com.aston.audioHosting.songs;


import com.aston.audioHosting.genre.Genre;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Song {

    long id;

    @NotNull(message = "Название не может быть пустым")
    String name;

    @NotNull(message = "Имя автора не может быть пустым")
    String author;

    @Size(min = 1, max = 200, message = "Максимальная длина описания — 200 символов")
    String description;


    LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    long duration;

    List<Genre> genres;

}
