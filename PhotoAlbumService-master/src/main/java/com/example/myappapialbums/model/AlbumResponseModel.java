package com.example.myappapialbums.model;

import lombok.Data;

@Data
public class AlbumResponseModel {   //전달
    private String albumId;
    private String userId; 
    private String name;
    private String description;

}
