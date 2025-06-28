package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.service.AdsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    public AdsController(AdsService adsService) {
        this.adsService = adsService;
    }

    // Получение всех объявлений
    @GetMapping
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    // Добавление объявления
    @PostMapping(path = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image) throws IOException {
        Ad result = adsService.addAd(properties, image);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Получение информации об объявлении
    @GetMapping("/{id}")
    public ExtendedAd getExtendedAdById(@PathVariable int id) throws ObjectNotFoundException {
        return adsService.getExtendedAdById(id);
    }

    // Удаление объявления
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(@PathVariable int id) throws ForbiddenException, ObjectNotFoundException {
        adsService.removeAd(id);
        return ResponseEntity.noContent().build();
    }

    // Обновление информации об объявлении
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd createOrUpdateAd) throws ForbiddenException, ObjectNotFoundException {
        Ad result = adsService.updateAds(id, createOrUpdateAd);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adsService.getAdsMe();
    }

    // Обновление картинки объявления
    @PatchMapping(path = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<byte[]> updateImage(@PathVariable int id, @RequestPart MultipartFile image) throws IOException, ForbiddenException, ObjectNotFoundException {
        return new ResponseEntity<>(adsService.updateImage(id, image), HttpStatus.OK);
    }
}
