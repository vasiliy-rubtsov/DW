package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

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
    public Ad addAd(@RequestPart("properties") CreateOrUpdateAd properties, @RequestPart("image") MultipartFile image) {
        return adsService.addAd(properties, image);
    }

    // Получение информации об объявлении
    @GetMapping("/{id}")
    public ExtendedAd getExtendedAdById(@PathVariable int id) {
        return adsService.getExtendedAdById(id);
    }

    // Удаление объявления
    @DeleteMapping("/id")
    public void removeAd(@PathVariable int id) {
        adsService.removeAd(id);
    }

    // Обновление информации об объявлении
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return adsService.updateAds(id, createOrUpdateAd);
    }

    // Получение объявлений авторизованного пользователя
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adsService.getAdsMe();
    }

    // Обновление картинки объявления
    @PatchMapping(path = "/{id}/image", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public byte[] updateImage(@PathVariable int id, @RequestPart MultipartFile image) {
        return adsService.updateImage(id, image);
    }
}
