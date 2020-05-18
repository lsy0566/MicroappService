package com.example.myappapialbums.data;

import lombok.Data;

@Data
public class AlbumEntity {  // 예시 jpa 연동시 Entity라는 어노테이션 있으면 됨
    private long id;
    private String albumId;
    private String userId; 
    private String name;
    private String description; 

}
