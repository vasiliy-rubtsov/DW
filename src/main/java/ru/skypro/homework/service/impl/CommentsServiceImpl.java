package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    public CommentsServiceImpl(CommentRepository commentRepository, AdsRepository adsRepository,  UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
    }

    // Получение комментариев объявления
    @Override
    public Comments getComments(long id) throws ObjectNotFoundException {
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        Comments comments = new Comments();

        for (CommentModel commentModel : adModel.getCommentModels()) {
            Comment comment = makeComment(commentModel);
            comments.addComment(comment);
        }

        return comments;
    }

    // Добавление комментария к объявлению
    @Override
    public Comment addComment(long id, CreateOrUpdateComment createdComment) throws ObjectNotFoundException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        // Ищем объявление, к которому надо добавить коммент
        AdModel adModel = adsRepository.findById(id).orElse(null);

        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        CommentModel commentModel = new CommentModel();
        commentModel.setAdModel(adModel);
        commentModel.setText(createdComment.getText());
        commentModel.setAuthor(userModel);
        commentModel.setCreatedAt(new Date());

        commentRepository.save(commentModel);

        return makeComment(commentModel);
    }

    // Удаление комментария
    @Override
    public void deleteComment(long adId, long commentId) {

    }

    // Обновление комментария
    @Override
    public Comment updateComment(long adId, long commentId, CreateOrUpdateComment updatedComment) throws ObjectNotFoundException, ForbiddenException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        // Ищем объявление
        AdModel adModel = adsRepository.findById(adId).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        CommentModel commentModel = commentRepository.findById(commentId).orElse(null);
        if (commentModel == null) {
            throw new ObjectNotFoundException();
        }

        if (adModel.getId() != commentModel.getAdModel().getId()) {
            throw new ObjectNotFoundException();
        }

        if (commentModel.getAuthor().getId() != userModel.getId() || !userModel.getRole().equals("ADMIN")) {
            // Если автор комментария не текущий пользователь или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        commentModel.setText(updatedComment.getText());

        commentRepository.save(commentModel);

        return makeComment(commentModel);
    }

    private Comment makeComment(CommentModel commentModel) {
        Comment result = new Comment();
        result.setPk(commentModel.getId());
        result.setAuthor(commentModel.getAuthor().getId());
        result.setText(commentModel.getText());
        result.setAuthorFirstName(commentModel.getAuthor().getFirstName());
        result.setAuthorImage("/images/" + commentModel.getAuthor().getImage());
        result.setCreatedAt(commentModel.getCreatedAt().getTime());

        return result;
    }
}
