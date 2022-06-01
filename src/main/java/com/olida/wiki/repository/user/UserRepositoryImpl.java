package com.olida.wiki.repository.user;

import com.olida.wiki.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @Autowired
    @Lazy
    UserRepository userRepository;

    @Override
    public User getByLogin(String login) {
        return this.userRepository.findAll().stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst()
                .orElse(null);
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }
}
