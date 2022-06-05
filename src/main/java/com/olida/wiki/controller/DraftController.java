package com.olida.wiki.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
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
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/articles/{article_id}/drafts")
public class DraftController {
    private UserService userService;
    private ArticleService articleService;
    private DraftService draftService;

    private JwtTokenRepository tokenRepository;


    public DraftController(UserService userService,
                           ArticleService articleService,
                           JwtTokenRepository jwtTokenRepository,
                           DraftService draftService) {
        this.userService = userService;
        this.articleService = articleService;
        this.tokenRepository = jwtTokenRepository;
        this.draftService = draftService;
    }


    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Object createDraft(@PathVariable(value="article_id") String article_id, @RequestBody com.olida.wiki.model.Draft draft, HttpServletResponse response) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        User user = userService.getByLogin(username);

        Date now = new Date();
        draft.setCreatedAt(new Timestamp(now.getTime()));
        Optional<Article> article = articleService.getOne(Integer.valueOf(article_id));
        article.ifPresent(draft::setArticle);
        draft.setAuthor(user);

        draft.setIsApproved(user.getIsadmin());
        return draftService.save(draft);
    }


    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Draft> getDrafts(@PathVariable(value="article_id") String article_id, HttpServletResponse response) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        User user = userService.getByLogin(username);

        Optional<Article> article = articleService.getOne(Integer.valueOf(article_id));
        if (article.isPresent()){
            List<Draft> drafts = draftService.getAllByArticleAndIsApproved(article.get(), true);
            if(user.getIsadmin()) {
                drafts.addAll(draftService.getAllByArticleAndIsApproved(article.get(), false));
            }
            return drafts;
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new ArrayList<>();
        }
    }

    EntityManager em;

    @PostMapping(path = "/{draft_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody com.olida.wiki.model.Draft changeDraftApproval(
            @PathVariable(value="draft_id") String draft_id,
            @RequestBody com.olida.wiki.model.Draft draft,
            HttpServletResponse response
    ) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        String bearerToken = request.getHeader("Authorization");

        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.tokenRepository.getSecret())
                .parseClaimsJws(bearerToken.split(" ")[1]);
        String username = jws.getBody().getSubject();
        User user = userService.getByLogin(username);

        if (user.getIsadmin()){
            Optional<Draft> requestedDraft = draftService.getOne(Integer.valueOf(draft_id));
            if (requestedDraft.isPresent()){
                if (requestedDraft.get().getIsApproved() != null) {
                    requestedDraft.get().setIsApproved(draft.getIsApproved());
                    List<Draft> approvedDrafts = draftService.getAllApprovedDraftsByUser(requestedDraft.get().getAuthor());
                    if (approvedDrafts.stream().count() > 3) {
                        requestedDraft.get().getAuthor().setIsadmin(true);
                        userService.saveUser(requestedDraft.get().getAuthor());
                    }
                }
                return draftService.save(requestedDraft.get());
            }
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new Draft();
    }
}