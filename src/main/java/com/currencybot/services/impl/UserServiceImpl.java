package com.currencybot.services.impl;

import com.currencybot.bot.BotState;
import com.currencybot.entities.User;
import com.currencybot.repository.UserRepository;
import com.currencybot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User saveOrGetUser(User user) {
        return userRepository.findById(user.getId())
                .orElseGet(() -> save(user));
    }
}
