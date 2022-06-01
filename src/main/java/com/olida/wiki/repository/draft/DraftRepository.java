package com.olida.wiki.repository.draft;

import com.olida.wiki.model.Draft;
import com.olida.wiki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DraftRepository extends JpaRepository<Draft, Integer> {

}
