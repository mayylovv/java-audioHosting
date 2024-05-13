package com.aston.audioHosting.songs;

import com.aston.audioHosting.exception.ObjectNotFoundException;
import com.aston.audioHosting.users.DBUserStorage;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SongService {

    private final DBSongStorage dbSongStorage;
    private final DBUserStorage dbUserStorage;

    @Autowired
    public SongService(DBSongStorage dbSongStorage, DBUserStorage dbUserStorage) {
        this.dbSongStorage = dbSongStorage;
        this.dbUserStorage = dbUserStorage;
    }

    public Song createSong(Song song) {
        Song createSong = dbSongStorage.createSong(song);
        log.info("Трек был добавлен");
        return createSong;
    }

    public Song getSong(String name) {
        Song song = dbSongStorage.getSong(name);
        if (!Objects.equals(song.getName(), name)) {
            throw new ObjectNotFoundException("Трек не найден");
        }
        log.info("Трек с названием: {}", name);
        return song;
    }

    public Collection<Song> getAllSongs() {
        Collection<Song> songs = dbSongStorage.getAllSongs();
        log.info("Все треки переданы.");
        return songs;
    }

    public Song updateSong(Song song) {
        Song updatedSong = dbSongStorage.updateSong(song);
        log.info("Трек {} был обновлен", song.getName());
        return updatedSong;
    }

    public Song deleteSong(String name) {
        Song deletedSong = dbSongStorage.deleteSong(name);
        log.info("Трек: {} был удален", name);
        return deletedSong;
    }

    public Song likeSong(long songId, long userId) {
        Song likedSong = dbSongStorage.likeSong(songId, userId);
        log.info("Пользователь добавил треку");
        return likedSong;
    }

    public Song removeLike(long songId, long userId) {
        Song removedLiked = dbSongStorage.removeLike(songId, userId);
        log.info("Пользователь удалил трек");
        return removedLiked;
    }

    public List<Song> getSongsOfUser(long userId) {
        List<Song> songs = dbSongStorage.getSongsOfUser(userId);
        log.info("Отправлен список треков пользователя");
        return songs;
    }

}
