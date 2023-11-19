package com.example.userregistration.service;

import com.example.userregistration.entity.UserEntity;

public interface UserService {

    UserEntity registerUser(UserEntity userRegistration);

    UserEntity editUser(UserEntity userEntity);
}
