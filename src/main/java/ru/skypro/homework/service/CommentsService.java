package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;

public interface CommentsService {
    // Получение комментариев объявления
    Comments getComments(int id);

    // Добавление комментария к объявлению
    Comment addComment(int id, CreateOrUpdateComment createdComment);

    // Удаление комментария
    void deleteComment(int adId, int commentId);

    // Обновление комментария
    Comment updateComment(int adId, int commentId, CreateOrUpdateComment updatedComment);

}
