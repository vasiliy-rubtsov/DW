package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.CommentModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentsService;

import java.util.Date;

@Service
public class CommentsServiceImpl implements CommentsService {

    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final CommentMapper commentMapper;

    public CommentsServiceImpl(CommentRepository commentRepository, AdsRepository adsRepository,  CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adsRepository = adsRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * Получение комментариев объявления
     * @param id long
     * @return Comments
     * @throws ObjectNotFoundException
     */
    @Override
    public Comments getComments(long id) throws ObjectNotFoundException {
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        return commentMapper.makeComments(adModel.getCommentModels());
    }

    /**
     * Добавление комментария к объявлению
     * @param id long
     * @param createdComment CreateOrUpdateComment
     * @return Comment
     * @throws ObjectNotFoundException
     */
    @Override
    public Comment addComment(long id, CreateOrUpdateComment createdComment) throws ObjectNotFoundException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        return commentMapper.makeComment(commentModel);
    }

    /**
     * Удаление комментария
     * @param adId long
     * @param commentId long
     * @throws ObjectNotFoundException
     * @throws ForbiddenException
     */
    @Override
    public void deleteComment(long adId, long commentId) throws ObjectNotFoundException, ForbiddenException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CommentModel commentModel = commentRepository.findById(commentId).orElse(null);
        if (commentModel == null) {
            throw new ObjectNotFoundException();
        }

        if (!(userModel.getRole().equals("ADMIN")  || userModel.getId() == commentModel.getAuthor().getId())) {
            throw new ForbiddenException();
        }

        commentRepository.delete(commentModel);
    }

    /**
     * Обновление комментария
     * @param adId long
     * @param commentId long
     * @param updatedComment CreateOrUpdateComment
     * @return Comment
     * @throws ObjectNotFoundException
     * @throws ForbiddenException
     */
    @Override
    public Comment updateComment(long adId, long commentId, CreateOrUpdateComment updatedComment) throws ObjectNotFoundException, ForbiddenException {
        UserModel userModel = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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

        if (!(commentModel.getAuthor().getId() == userModel.getId() || userModel.getRole().equals("ADMIN"))) {
            // Если автор комментария не текущий пользователь или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        commentModel.setText(updatedComment.getText());

        commentRepository.save(commentModel);

        return commentMapper.makeComment(commentModel);
    }
}
