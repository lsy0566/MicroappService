package com.example.myappapialbums.service;

import com.example.myappapialbums.data.AlbumEntity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AlbumServiceImpl implements AlbumService {
    AlbumEntity albumEntity;

    @Override
    public AlbumEntity getAlbums(String id) {
        return albumEntity;
    }
}
