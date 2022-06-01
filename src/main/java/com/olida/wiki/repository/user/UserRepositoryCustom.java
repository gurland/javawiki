package com.olida.wiki.repository.user;

import com.olida.wiki.model.User;

public interface UserRepositoryCustom {
    public User getByLogin(String login);
}
