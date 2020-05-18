package com.example.myappapialbums.data;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table
public class AlbumEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String albumId;
    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 100)
    private String description;
}
