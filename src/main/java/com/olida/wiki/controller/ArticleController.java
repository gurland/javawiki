package com.olida.wiki.controller;


import com.olida.wiki.model.Article;
import com.olida.wiki.model.Draft;
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
import java.sql.Timestamp;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/articles")
public class ArticleController {
    private UserService userService;
    private ArticleService articleService;

    private JwtTokenRepository tokenRepository;
    private DraftService draftService;


    public ArticleController(UserService userService, ArticleService articleService, JwtTokenRepository jwtTokenRepository, DraftService draftService) {
        this.userService = userService;
        this.articleService = articleService;
        this.tokenRepository = jwtTokenRepository;
        this.draftService = draftService;
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
            Article createdArticle = articleService.save(article);
            Draft freshDraft = new Draft();
            freshDraft.setArticle(createdArticle);
            freshDraft.setAuthor(user);
            freshDraft.setIsApproved(true);
            freshDraft.setFirst("");
            freshDraft.setSecond("");
            freshDraft.setThird("");
            Date now = new Date();
            freshDraft.setCreatedAt(new Timestamp(now.getTime()));
            draftService.save(freshDraft);
            return createdArticle;

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

    @GetMapping(path = "/{article_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Optional<Article> getArticle(@PathVariable(value="article_id") String article_id) {
        return articleService.getOne(Integer.valueOf(article_id));
    }

    @PutMapping(path = "/{article_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object editArticle(@PathVariable(value="article_id") String article_id, @RequestBody Article newArticle) {
        Optional<Article> existingArticle = articleService.getOne(Integer.valueOf(article_id));
        if (existingArticle.isPresent()){
            existingArticle.get().setCategory(newArticle.getCategory());
            existingArticle.get().setImage(newArticle.getImage());
            existingArticle.get().setTitle(newArticle.getTitle());
            return this.articleService.save(existingArticle.get());
        }

        return null;
    }
}