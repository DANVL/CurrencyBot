package com.currencybot.services.impl;

import com.currencybot.entities.User;
import com.currencybot.repository.UserRepository;
import com.currencybot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        User result = userRepository.save(user);
        log.info(String.format("Saved new user with id: %d", user.getId()));
        return result;
    }

    @Override
    public User saveOrGetUser(User user) {

        Optional<User> result = userRepository.findById(user.getId());

        if(result.isPresent()){
            log.info(String.format("Returning existing user with id: %d",user.getId()));
            return result.get();
        }else{
            log.info("User not found, creating new");
            return save(user);
        }
    }
}
