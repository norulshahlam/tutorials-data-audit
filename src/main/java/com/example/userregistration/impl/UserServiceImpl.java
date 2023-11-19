package com.example.userregistration.impl;

import com.example.userregistration.entity.UserEntity;
import com.example.userregistration.repository.UserRepository;
import com.example.userregistration.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author norulshahlam.mohsen
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserEntity registerUser(UserEntity request) {
        return repository.save(request);
    }

    @Override
    public UserEntity editUser(UserEntity request) {
        Optional<UserEntity> entity = repository.findById(request.getCustomerEmail());

        if (entity.isPresent()) {
            UserEntity userEntity = entity.get();
            return repository.save(userEntity);
        }
        return null;
    }
}
