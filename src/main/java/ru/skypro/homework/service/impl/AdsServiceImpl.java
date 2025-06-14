package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

@Service
public class AdsServiceImpl implements AdsService {
    // Получение всех объявлений
    @Override
    public Ads getAllAds() {
        Ads ads = new Ads();
        return ads;
    }

    // Добавление объявления
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {
        Ad ad = new Ad();

        return ad;
    }

    // Получение информации об объявлении
    @Override
    public ExtendedAd getExtendedAdById(int id) {
        ExtendedAd extendedAd = new ExtendedAd();
        return extendedAd;
    }

    // Удаление объявления
    @Override
    public void removeAd(int id) {

    }

    // Обновление информации об объявлении
    @Override
    public Ad updateAds(int id, CreateOrUpdateAd createOrUpdateAd) {
        Ad ad = new Ad();
        return ad;
    }

    // Получение объявлений авторизованного пользователя
    @Override
    public Ads getAdsMe() {
        Ads ads = new Ads();
        return ads;
    }

    // Обновление картинки объявления
    @Override
    public byte[] updateImage(int id, MultipartFile image) {
        byte[] imageBytes = new byte[0];
        return imageBytes;
    }
}
