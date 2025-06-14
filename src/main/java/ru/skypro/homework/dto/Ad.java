package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Ad {
    @JsonProperty("author")
    private int author;	// id автора объявления

    @JsonProperty("image")
    private String image;	// ссылка на картинку объявления

    @JsonProperty("pk")
    private int pk;	// id объявления

    @JsonProperty("price")
    private int price;	// цена объявления

    @JsonProperty("title")
    private String title;	// заголовок объявления
}
