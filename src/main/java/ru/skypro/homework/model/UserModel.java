package ru.skypro.homework.model;

import lombok.Data;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;	        // id пользователя
    private String email;	    // логин пользователя
    private String firstName;	// имя пользователя
    private String lastName;	// фамилия пользователя
    private String phone;	    // телефон пользователя
    private String role;	    // роль пользователя
    private String image;	    // ссылка на аватар пользователя

    @OneToMany(mappedBy = "author", cascade =  CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AdModel> adModels;       // объявления пользователя

    @OneToMany(mappedBy = "author", cascade =  CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<CommentModel> commentModels; // Комментарии пользователя


}
