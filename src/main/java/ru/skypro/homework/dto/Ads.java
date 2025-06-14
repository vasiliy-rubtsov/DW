package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import lombok.Data;

@Data
public class Ads {
    @JsonProperty("count")
    private int count;	// общее количество объявлений
    @JsonProperty("results")
    private List<Ad> results; // список объявлений
}

