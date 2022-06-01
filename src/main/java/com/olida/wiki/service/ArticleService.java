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
import java.util.Optional;

@Service
public class ArticleService {

    private ArticleRepository repository;

    public ArticleService(ArticleRepository repository) {
        this.repository = repository;
    }

    public List<Article> getAll() {
        return this.repository.findAll();
    }

    public Optional<Article> getOne(Integer id){
        return this.repository.findById(id);
    }

    public List<Article> getAllByCategory(String category) {
        return this.repository.findByCategory(category);
    }

    public Article save(Article article){
        return this.repository.save(article);
    }
}
