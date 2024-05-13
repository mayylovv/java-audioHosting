package com.aston.audioHosting.genre;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{name}")
    public Genre getGerne(@PathVariable String name) {
        return genreService.getGerne(name);
    }

    @GetMapping
    public Collection<Genre> getAllGernes() {
        return genreService.getAllGernes();
    }

}
