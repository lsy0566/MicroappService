package com.example.myappapialbums.controller;

import com.example.myappapialbums.data.AlbumEntity;
import com.example.myappapialbums.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AlbumController {
    @Autowired
    AlbumService albumService;
    @Autowired
    AlbumEntity albumEntity;

    @GetMapping("/{id}/albums")
    public AlbumEntity userAlbums(@PathVariable("id") String id) {

    return albumService.getAlbums(id);
    }
}
