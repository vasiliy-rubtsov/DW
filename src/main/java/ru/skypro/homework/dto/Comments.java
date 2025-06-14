package ru.skypro.homework.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import lombok.Data;

@Data
public class Comments {
    @JsonProperty("count")
    private int count;	// общее количество комментариев
    @JsonProperty("results")
    private List<Comment> results;	// список комментариев
}
