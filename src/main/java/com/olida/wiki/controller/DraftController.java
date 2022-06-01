package com.olida.wiki.controller;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.User;
import com.olida.wiki.security.JwtTokenRepository;
import com.olida.wiki.service.ArticleService;
import com.olida.wiki.service.DraftService;
import com.olida.wiki.service.UserService;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.apache.catalina.connector.Request;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.lang.model.type.UnionType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/articles/{article_id}/drafts")
public class DraftController {
    private UserService userService;
    private ArticleService articleService;
    private DraftService draftService;

    private JwtTokenRepository tokenRepository;


    public DraftController(UserService userService, ArticleService articleService, JwtTokenRepository jwtTokenRepository) {
        this.userService = userService;
        this.articleService = articleService;
        this.tokenRepository = jwtTokenRepository;
    }


    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object creaateArticle(@RequestBody com.olida.wiki.model.Article article, HttpServletResponse response) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        User user = userService.getByLogin(username);

        if (user.getIsadmin()){
            return articleService.save(article);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return articleService.getAll();
        }
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Article> getArticles(@RequestParam String category) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        User user = userService.getByLogin(username);

        if(user.getIsadmin()){
            return articleService.getAllByCategory(category);
        } else {
            return articleService.getAllByCategory(category);
        }
    }

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody com.olida.wiki.model.User add(@RequestBody com.olida.wiki.model.User user, HttpServletResponse response) {
        try {
            return userService.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return user;
        }
    }
}