package com.olida.wiki.controller;

import com.olida.wiki.model.User;
import com.olida.wiki.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(path = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<User> getAll() {
        return this.service.getAll();
    }

    @GetMapping(path = "/users/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody User getUserById(@PathVariable(value="login") String login) {
        return this.service.getByLogin(login);
    }
}

//import com.olida.wiki.model.User;
//import com.olida.wiki.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//
//@RestController
//@RequestMapping("/users")
//public class UserController {
//    @Autowired
//    UserService userService;
//
//    @GetMapping("")
//    public List<User> list() {
//        return userService.listAllUser();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> get(@PathVariable Integer id) {
//        try {
//            User user = userService.getUser(id);
//            return new ResponseEntity<User>(user, HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @PostMapping("/")
//    public void add(@RequestBody User user) {
//        userService.saveUser(user, user.password);
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer id) {
//        try {
//            User existUser = userService.getUser(id);
//            user.setId(id);
//            userService.saveUser(user, user.password);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable Integer id) {
//
//        userService.deleteUser(id);
//    }
//}