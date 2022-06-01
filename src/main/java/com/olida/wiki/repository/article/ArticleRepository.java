package com.olida.wiki.repository.article;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    public List<Article> findByCategory(String category);
}
