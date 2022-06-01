package com.olida.wiki.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@DynamicUpdate
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", length = 128, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 128, nullable = false, unique = false)
    private String password;

    @Column(name = "first_name", length = 128, nullable = false, unique = false)
    private String firstname;

    @Column(name = "last_name", length = 128, nullable = false, unique = false)
    private String lastname;

    @Column(name = "is_admin", columnDefinition = "tinyint default 0")
    private Boolean isadmin;
}
