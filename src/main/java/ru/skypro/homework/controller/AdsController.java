package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Объявления")
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    // Получение всех объявлений
    @Operation(summary = "Получение всех объявлений")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class)))
    })
    @GetMapping
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    // Добавление объявления
    @Operation(summary = "Добавление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Unautorized", content = @Content)
    })
    @PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image) throws IOException {
        return adsService.addAd(properties, image);
    }

    // Получение информации об объявлении
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))),
            @ApiResponse(responseCode = "401", description = "Unautorized", content = @Content), @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    })
    @GetMapping("/{id}")
    public ExtendedAd getExtendedAdById(@PathVariable int id) throws ObjectNotFoundException {
        return adsService.getExtendedAdById(id);
    }

    // Удаление объявления
    @Operation(summary = "Удаление объявления")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No Content",  content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAd(@PathVariable int id) throws ForbiddenException, ObjectNotFoundException {
        adsService.removeAd(id);
    }

    // Обновление информации об объявлении
    @Operation(summary = "Обновление информации об объявлении")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Ad.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd createOrUpdateAd) throws ForbiddenException, ObjectNotFoundException {
        return adsService.updateAds(id, createOrUpdateAd);
    }

    // Получение объявлений авторизованного пользователя
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",  content = @Content(mediaType = "application/json",  schema = @Schema(implementation = Ads.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content)
    })
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adsService.getAdsMe();
    }

    // Обновление картинки объявления
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/octet-stream", array = @ArraySchema(schema = @Schema(implementation = byte[].class)))),
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/octet-stream", array = @ArraySchema(schema = @Schema(implementation = byte.class)))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",  content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden",  content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",  content = @Content)
    })
    @PatchMapping(path = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public byte[] updateImage(@PathVariable int id, @RequestPart MultipartFile image) throws IOException, ForbiddenException, ObjectNotFoundException {
        return adsService.updateImage(id, image);
    }
}
