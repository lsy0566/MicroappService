package com.example.myappapiusers.controller;

import com.example.myappapiusers.data.UserEntity;
import com.example.myappapiusers.model.CreateUserRequestModel;
import com.example.myappapiusers.model.CreateUserResponseModel;
import com.example.myappapiusers.repository.UserRepository;
import com.example.myappapiusers.service.UserService;
import com.example.myappapiusers.shared.UserDto;
import com.netflix.discovery.converters.Auto;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    Environment env;

    @Autowired
    UserService userService;


    @Autowired
    UserRepository repository;

    @GetMapping("/status/check")    //랜덤포트를 정확히 알 수 있음
    public String status() {
        return String.format("Users-WS] Working on port %s, secret=%s",
                env.getProperty("local.server.port"),
                env.getProperty("token.secret"));
    }

    // 사용자 추가 API
//    @PostMapping
//    public List<UserEntity> insert(@RequestBody UserEntity userentity) {
//        repository.save(userentity);
//
//        return repository.findAll();
//    }

    //
    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<CreateUserResponseModel> createUsers(@Valid @RequestBody CreateUserRequestModel userDetails){
        //CreateUserRequestModel -> UserDto (using ModelMapper)
//        System.out.println(req.getRemoteAddr());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(
                MatchingStrategies.STRICT);
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);
        UserDto createdDto = userService.createUser(userDto);

//        return new ResponseEntity((HttpStatus.CREATED));
        CreateUserResponseModel returnValue = modelMapper.map(createdDto,
                CreateUserResponseModel.class);
        // firstName, lastName, email, userId 클래스 생성해보기

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }


    // 사용자 조회
//    @GetMapping
//    public List<UserEntity> getAllBy() {
//
//        return repository.findAll();
//    }
}
