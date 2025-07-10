package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.service.CommentsService;

@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Комментарии")
@RestController
@RequestMapping("/ads")
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    // Получение комментариев объявления
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Comments.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable("id") int id) throws ObjectNotFoundException {
        return commentsService.getComments(id);
    }

    // Добавление комментария к объявлению
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable("id") int id, @RequestBody CreateOrUpdateComment createdComment) throws ObjectNotFoundException {
        return commentsService.addComment(id, createdComment);
    }

    // Удаление комментария
    @Operation(summary = "Удаление комментария")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public void deleteComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId) throws ForbiddenException, ObjectNotFoundException {
        commentsService.deleteComment(adId, commentId);
    }

    // Обновление комментария
    @Operation(summary = "Обновление комментария")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class) )),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public Comment updateComment(@PathVariable("adId") int adId, @PathVariable("commentId") int commentId, @RequestBody CreateOrUpdateComment updatedComment) throws ObjectNotFoundException, ForbiddenException {
        return commentsService.updateComment(adId, commentId, updatedComment);
    }
}
