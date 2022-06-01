package com.olida.wiki;

import com.olida.wiki.model.User;
import com.olida.wiki.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DBSeeder implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setLogin("admin");
            user.setPassword("admin");
            user.setFirstname("Admin");
            user.setLastname("Lastname");
            user.setIsadmin(true);
//          user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            userRepository.save(user);
        }
        System.out.println(userRepository.count());
    }
}