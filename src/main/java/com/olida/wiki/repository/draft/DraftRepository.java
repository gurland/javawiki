package com.olida.wiki.repository.draft;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.Draft;
import com.olida.wiki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DraftRepository extends JpaRepository<Draft, Integer> {

    List<Draft> findByArticleAndIsApprovedIsNull(@Param("article") Article article);


    List<Draft> findByArticleAndIsApproved(Article article, boolean isApproved);

    List<Draft> findByAuthorAndIsApproved(User author, boolean isApproved);
}
