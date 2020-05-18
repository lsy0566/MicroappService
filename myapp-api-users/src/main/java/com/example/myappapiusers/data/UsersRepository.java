package com.example.myappapiusers.data;

import com.example.myappapiusers.model.UserResponseModel;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    // select * from users where email=?
    UserResponseModel findById(String id);

    UserEntity findByUserId(String userId);
}
