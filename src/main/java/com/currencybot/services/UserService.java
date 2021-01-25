package com.currencybot.services;

import com.currencybot.entities.User;

public interface UserService {
    User save(User user);

    User saveOrGetUser(User user);
}
