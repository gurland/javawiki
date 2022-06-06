package com.olida.wiki.service;

import com.olida.wiki.model.Article;
import com.olida.wiki.model.Draft;
import com.olida.wiki.model.User;
import com.olida.wiki.repository.draft.DraftRepository;
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

    public List<Draft> getAllByArticleAndIsApproved(Article article, Boolean isApproved) {
        if (isApproved != null){
            return this.draftRepository.findByArticleAndIsApproved(article, isApproved);
        } else {
            return this.draftRepository.findByArticleAndIsApprovedIsNull(article);
        }
    }

    public List<Draft> getAllApprovedDraftsByUser(User user) {
        return this.draftRepository.findByAuthorAndIsApproved(user, true);
    }

    public Optional<Draft> getOne(Integer id){
        return this.draftRepository.findById(id);
    }

    public Draft save(Draft draft){
        return this.draftRepository.save(draft);
    }
}
