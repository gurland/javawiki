package com.olida.wiki.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "drafts")
@DynamicUpdate
public class Draft {
    @Id
    @Column(name = "draft_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "first", nullable = true, unique = false, columnDefinition = "TEXT")
    private String first;

    @Column(name = "second", nullable = true, unique = false, columnDefinition = "TEXT")
    private String second;

    @Column(name = "third", nullable = true, unique = false, columnDefinition = "TEXT")
    private String third;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "created_at", nullable = false, unique = false)
    private Timestamp createdAt;

    @Column(name = "is_approved", nullable = true, unique = false)
    private Boolean isApproved = null;
}
