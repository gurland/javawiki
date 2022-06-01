package com.olida.wiki.service;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.Draft;
import com.olida.wiki.repository.article.ArticleRepository;
import com.olida.wiki.repository.draft.DraftRepository;
import com.olida.wiki.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<Draft> getOne(Integer id){
        return this.draftRepository.findById(id);
    }

    public Draft save(Draft draft){
        return this.draftRepository.save(draft);
    }
}
