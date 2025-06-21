package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdsServiceImpl implements AdsService {
    // Получение всех объявлений
    @Override
    public Ads getAllAds() {
        return new Ads();
    }

    // Добавление объявления
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) {
        return new Ad();
    }

    // Получение информации об объявлении
    @Override
    public ExtendedAd getExtendedAdById(int id) {
        return new ExtendedAd();
    }

    // Удаление объявления
    @Override
    public void removeAd(int id) {

    }

    // Обновление информации об объявлении
    @Override
    public Ad updateAds(int id, CreateOrUpdateAd createOrUpdateAd) {
        return new Ad();
    }

    // Получение объявлений авторизованного пользователя
    @Override
    public Ads getAdsMe() {
        return new Ads();
    }

    // Обновление картинки объявления
    @Override
    public byte[] updateImage(int id, MultipartFile image) throws IOException {
        List<String> result = new ArrayList<>();
        result.add("Строка 1");
        result.add("Строка 2");
        result.add("Строка 3");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        for (String line : result) {
            dos.writeUTF(line);
        }

        return baos.toByteArray();
    }

    private Ads makeAds() {
        Ads ads = new Ads();

        Ad ad = new Ad();
        ad.setPk(1);
        ad.setAuthor(1);
        ad.setPrice(2500);
        ad.setTitle("Суперприбор");
        ads.getResults().add(ad);

        ad = new Ad();
        ad.setPk(2);
        ad.setAuthor(1);
        ad.setPrice(3200);
        ad.setTitle("Мегаприбор");
        ads.getResults().add(ad);

        ad = new Ad();
        ad.setPk(3);
        ad.setAuthor(1);
        ad.setPrice(1000);
        ad.setTitle("Простой прибор");
        ads.getResults().add(ad);

        ads.setCount(3);
        return ads;
    }
}
