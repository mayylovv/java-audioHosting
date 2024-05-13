package com.aston.audioHosting.songs;

import java.util.Collection;
import java.util.List;

public interface SongStorage {

    Song createSong(Song song);

    Song getSong(String name);

    Collection<Song> getAllSongs();

    Song updateSong(Song song);

    Song deleteSong(String name);

    Song likeSong(long songId, long userId);

    Song removeLike(long songId, long userId);

    List<Song> getSongsOfUser(long userId);
}
