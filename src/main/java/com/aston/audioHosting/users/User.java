package com.aston.audioHosting.users;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    long id;

    @Email(message = "В адресе электронной почты ошибка")
    String email;


    @Size(min = 1, max = 20)
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    String login;

    String name;

    @NotNull(message = "Не указана дата рождения")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;

}
