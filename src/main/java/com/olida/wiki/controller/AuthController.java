package com.olida.wiki.controller;

import com.olida.wiki.security.JwtTokenRepository;
import com.olida.wiki.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private JwtTokenRepository tokenRepository;

    private String generateJWT(com.olida.wiki.model.User user) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(3600)
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setId(id)
                .setSubject(user.getLogin())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .claim("isAdmin", user.getIsadmin())
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
    }

    private UserService userService;

    public AuthController(UserService service, JwtTokenRepository jwtTokenRepository) {
        this.userService = service;
        this.tokenRepository = jwtTokenRepository;
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object getAuthUser(@RequestBody (required = false) com.olida.wiki.model.User newUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();
        User user = (principal instanceof User) ? (User) principal : null;

        com.olida.wiki.model.User currentUser;

        try {
            if (Objects.equals(newUser.getFirstname(), "login")) {
                currentUser = userService.getByLogin(user.getUsername());
            } else if (Objects.equals(newUser.getFirstname(), "register")) {
                newUser.setIsadmin(false);
                userService.saveUser(newUser);
                currentUser = newUser;
            } else {
                return null;
            }

            HashMap<String, String> token_json = new HashMap<String, String>();
            token_json.put("token", this.generateJWT(currentUser));
            return token_json;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object refreshJWTToken() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        com.olida.wiki.model.User user = userService.getByLogin(username);

        HashMap<String, String> token_json = new HashMap<String, String>();
        token_json.put("token", this.generateJWT(user));
        return token_json;

    }
}