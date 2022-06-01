package com.olida.wiki.service;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.Draft;
import com.olida.wiki.repository.article.ArticleRepository;
import com.olida.wiki.repository.draft.DraftRepository;
import com.olida.wiki.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftService {

    private DraftRepository draftRepository;

    public DraftService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public List<Draft> getAll() {
        return this.draftRepository.findAll();
    }

    public List<Draft> getAllByArticleAndIsApproved(Article article, boolean isApproved) {
        return this.draftRepository.findByArticleAndIsApproved(article, isApproved);
    }

    public Draft save(Draft draft){
        return this.draftRepository.save(draft);
    }
}
