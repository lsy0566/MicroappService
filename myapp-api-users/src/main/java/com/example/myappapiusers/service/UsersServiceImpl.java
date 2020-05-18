package com.example.myappapiusers.service;

import com.example.myappapiusers.client.AlbumServiceClient;
import com.example.myappapiusers.data.UserEntity;
import com.example.myappapiusers.data.UsersRepository;
import com.example.myappapiusers.model.AlbumResponseModel;
import com.example.myappapiusers.shared.UserDto;
import com.netflix.discovery.shared.Application;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UserService {
    UsersRepository repository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
//    RestTemplate restTemplate;
    AlbumServiceClient albumServiceClient;


    @Autowired
    public UsersServiceImpl(UsersRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder, AlbumServiceClient albumServiceClient) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.albumServiceClient = albumServiceClient;   // 해당하는 bean의 값을 여기에 등록 * Bean이 두개이면 qualified를 사용해야 함
    }

    @Override
    public UserDto createUser(UserDto userDetails) {

        userDetails.setUserId(UUID.randomUUID().toString());

        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        userEntity.setEncryptedPassword(userDetails.getEncryptedPassword());


        repository.save(userEntity);

        UserDto returnValue = modelMapper.map(userEntity, UserDto.class);
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = repository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = repository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        // UserEntity -> UserDto (using ModelMapper)

        return new ModelMapper().map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = repository.findByUserId(userId); // find 뒤에는 컬럼명이어야 한다.

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        // call -> albums microservice
//        ResponseEntity<List<AlbumResponseModel>> albumsListResponse =
//        restTemplate.exchange(
//                String.format("http://albums-ws/users/%s/albums", userId),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//                });
//
//        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
        List<AlbumResponseModel> albumsList = albumServiceClient.getAlbums(userId);
        userDto.setAlbums(albumsList);

        return userDto;
    }
}