package com.aston.audioHosting.songs;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final SongService songService;

    @Autowired
    public SongController(com.aston.audioHosting.songs.SongService songService) {
        this.songService = songService;
    }

    @PostMapping
    public Song createSong(@Valid @RequestBody Song song) {
        return songService.createSong(song);
    }

    @GetMapping("/{name}")
    public Song getFilm(@PathVariable String name) {
        return songService.getSong(name);
    }

    @GetMapping
    public Collection<Song> getAllSongs() {
        return songService.getAllSongs();
    }

    @PutMapping
    public Song updateSong(@Valid @RequestBody Song song) {
        return songService.updateSong(song);
    }

    @DeleteMapping("/{name}")
    public Song deleteSong(@PathVariable String name) {
        return songService.deleteSong(name);
    }

    @PutMapping("/{songId}/like/{userId}")
    public Song likeSong(@PathVariable long songId, @PathVariable long userId) {
        return songService.likeSong(songId,userId);
    }

    @DeleteMapping("/{songId}/like/{userId}")
    public Song removeLike(@PathVariable long songId, @PathVariable long userId) {
        return songService.removeLike(songId, userId);
    }

    @GetMapping("/like/{userId}")
    public List<Song> getSongsOfUser(@PathVariable long userId) {
        return songService.getSongsOfUser(userId);
    }

}
