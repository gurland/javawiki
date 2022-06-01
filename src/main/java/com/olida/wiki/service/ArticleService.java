package com.olida.wiki.service;

import com.olida.wiki.model.Article;
import com.olida.wiki.repository.article.ArticleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ArticleService {

    private ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<Article> getAll() {
        return this.repository.findAll();
    }

    public List<Article> getAllByCategory(String category) {
        return this.repository.findByCategory(category);
    }

    public Article save(Article article){
        return this.repository.save(article);
    }
}
