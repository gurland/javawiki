package com.olida.wiki.repository.user;

import com.olida.wiki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    List<User> findByLogin(String login);
}
