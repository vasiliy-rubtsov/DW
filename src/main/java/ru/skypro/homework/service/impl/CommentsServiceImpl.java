package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentsService;

@Service
public class CommentsServiceImpl implements CommentsService {
    // Получение комментариев объявления
    @Override
    public Comments getComments(int id) {
        return new Comments();
    }

    // Добавление комментария к объявлению
    @Override
    public Comment addComment(int id, CreateOrUpdateComment createdComment) {
        return new Comment();
    }

    // Удаление комментария
    @Override
    public void deleteComment(int adId, int commentId) {

    }

    // Обновление комментария
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updatedComment) {
        return new Comment();
    }
}
