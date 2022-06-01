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
@Table(name = "articles")
@DynamicUpdate
public class Article {
    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "image", length = 128, nullable = false, unique = false)
    private String image;

    @Column(name = "title", length = 200, nullable = false, unique = false)
    private String title;

    @Column(name = "category", length = 128, nullable = false, unique = false)
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
