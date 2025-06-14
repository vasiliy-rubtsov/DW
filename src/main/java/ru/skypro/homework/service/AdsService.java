package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;

public interface AdsService {
    // Получение всех объявлений
    Ads getAllAds();

    // Добавление объявления
    Ad addAd(CreateOrUpdateAd properties, MultipartFile image);

    // Получение информации об объявлении
    ExtendedAd getExtendedAdById(int id);

    // Удаление объявления
    void removeAd(int id);

    // Обновление информации об объявлении
    Ad updateAds(int id, CreateOrUpdateAd createOrUpdateAd);

    // Получение объявлений авторизованного пользователя
    Ads getAdsMe();

    // Обновление картинки объявления
    byte[] updateImage(int id, MultipartFile image);
}
