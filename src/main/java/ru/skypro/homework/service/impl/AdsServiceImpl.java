package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.ForbiddenException;
import ru.skypro.homework.exception.ObjectNotFoundException;
import ru.skypro.homework.model.AdModel;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.ImagesService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
public class AdsServiceImpl implements AdsService {

    Logger logger = LoggerFactory.getLogger(AdsServiceImpl.class);
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;
    private final UserDetailsManager userDetailsManager;
    private final ImagesService imagesService;

    @Value("${ads.image-file-dir}")
    private String imageFileDir;

    public AdsServiceImpl(AdsRepository adsRepository, UserRepository userRepository, UserDetailsManager userDetailsManager,  ImagesService imagesService) {
        this.adsRepository = adsRepository;
        this.userRepository = userRepository;
        this.userDetailsManager = userDetailsManager;
        this.imagesService = imagesService;
    }

    // Получение всех объявлений
    @Override
    public Ads getAllAds() {
        List<AdModel> ads = adsRepository.findAll();
        return makeAdsResult(ads);
    }

    // Добавление объявления
    @Override
    public Ad addAd(CreateOrUpdateAd properties, MultipartFile image) throws IOException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        AdModel adModel = new AdModel();
        adModel.setTitle(properties.getTitle());
        adModel.setDescription(properties.getDescription());
        adModel.setPrice(properties.getPrice());
        adModel.setAuthor(userModel);

        adsRepository.save(adModel);

        // обрабатываем полученный файл
        String targetFileName = imagesService.makeTargetFileName(Objects.requireNonNull(image.getOriginalFilename()), adModel.getId(), FileTypeEnum.PRODUCT);
        imagesService.saveFileToDisk(image, targetFileName);
        adModel.setImage(targetFileName);
        adsRepository.save(adModel);

        return makeAdResult(adModel);
    }

    // Получение информации об объявлении
    @Override
    public ExtendedAd getExtendedAdById(long id) throws ObjectNotFoundException {
        ExtendedAd result = new ExtendedAd();

        AdModel adModel = adsRepository.findById(id).orElse(null);

        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        result.setImage("/images/" + adModel.getImage());
        result.setTitle(adModel.getTitle());
        result.setPrice(adModel.getPrice());
        result.setDescription(adModel.getDescription());
        result.setPk(adModel.getId());

        UserModel author = adModel.getAuthor();
        result.setEmail(author.getEmail());
        result.setPhone(author.getPhone());
        result.setAuthorFirstName(author.getFirstName());
        result.setAuthorLastName(author.getLastName());

        return result;
    }

    // Удаление объявления
    @Override
    public void removeAd(long id) throws ForbiddenException, ObjectNotFoundException {
        long userId = 1L;

        // Находим удаляемое объявление в БД по его ID
        AdModel adModel = adsRepository.findById(id).orElse(null);

        if (adModel == null) {
            // объявление не найдено - ошибка 404
            throw new ObjectNotFoundException();
        }

        // Проверяем, принадлежит ли удаляемое объявление текущему пользователю
        long adUserId = adModel.getAuthor().getId();
        if (adUserId != userId) {
            // не принадлежит - ошибка 403
            throw new ForbiddenException();
        }

        adsRepository.delete(adModel);
    }

    // Обновление информации об объявлении
    @Override
    public Ad updateAds(long id, CreateOrUpdateAd createOrUpdateAd) throws ForbiddenException, ObjectNotFoundException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        // Ищем объявление
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        if (adModel.getAuthor().getId() != userModel.getId() || !userModel.getRole().equals("ADMIN")) {
            // Если текущий пользователь не автор этого объявления или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        adModel.setTitle(createOrUpdateAd.getTitle());
        adModel.setDescription(createOrUpdateAd.getDescription());
        adModel.setPrice(createOrUpdateAd.getPrice());

        adsRepository.save(adModel);

        return makeAdResult(adModel);
    }

    // Получение объявлений авторизованного пользователя
    @Override
    public Ads getAdsMe() {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        List<AdModel> ads = adsRepository.findByAuthorId(userId);
        return makeAdsResult(ads);
    }

    // Обновление картинки объявления
    @Override
    public byte[] updateImage(long id, MultipartFile image) throws IOException, ObjectNotFoundException, ForbiddenException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        // Ищем объявление
        AdModel adModel = adsRepository.findById(id).orElse(null);
        if (adModel == null) {
            throw new ObjectNotFoundException();
        }

        if (adModel.getAuthor().getId() != userModel.getId() || !userModel.getRole().equals("ADMIN")) {
            // Если текущий пользователь не автор этого объявления или не админ, то запрещаем редактирование
            throw new ForbiddenException();
        }

        String targetFileName = imagesService.makeTargetFileName(Objects.requireNonNull(image.getOriginalFilename()), adModel.getId(), FileTypeEnum.PRODUCT);

        imagesService.saveFileToDisk(image, targetFileName);
        adModel.setImage(targetFileName);
        adsRepository.save(adModel);

        Path filePath = Path.of(imageFileDir, targetFileName);
        return Files.readAllBytes(filePath);
    }

    private Ads makeAdsResult(List<AdModel> ads) {
        Ads result = new Ads();
        Ad ad;
        for (AdModel adModel : ads) {
            ad =  new Ad();
            ad.setImage("/images/" + adModel.getImage());
            ad.setTitle(adModel.getTitle());
            ad.setPk(adModel.getId());
            ad.setPrice(adModel.getPrice());
            ad.setAuthor(adModel.getAuthor().getId());
            result.addAd(ad);
        }

        return result;
    }

    private Ad makeAdResult(AdModel ad) {
        Ad result = new Ad();

        result.setImage("/images/" + ad.getImage());
        result.setAuthor(ad.getAuthor().getId());
        result.setTitle(ad.getTitle());
        result.setPrice(ad.getPrice());
        result.setPk(ad.getId());

        return result;
    }

}
