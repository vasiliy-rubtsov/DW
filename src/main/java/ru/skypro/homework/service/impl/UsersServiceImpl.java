package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.FileTypeEnum;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImagesService;
import ru.skypro.homework.service.UsersService;

import java.io.IOException;

@Service
public class UsersServiceImpl implements UsersService {

    @Value("${ads.image-file-dir}")
    private String imageFileDir;
    private final ImagesService imagesService;
    private final UserRepository userRepository;

    public UsersServiceImpl(ImagesService imagesService, UserRepository userRepository) {
        this.imagesService = imagesService;
        this.userRepository = userRepository;
    }

    // Обновление пароля
    @Override
    public void setPassword(NewPassword newPassword) {

    }

    // Получение информации об авторизованном пользователе
    @Override
    public User getUser() {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        User user = new User();
        user.setImage("/images/" + userModel.getImage());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setPhone(userModel.getPhone());
        user.setRole(userModel.getRole());
        user.setId(userModel.getId());

        return user;
    }

    // Обновление информации об авторизованном пользователе
    @Override
    public UpdateUser updateUser(UpdateUser user) {
        return user;
    }

    // Обновление аватара авторизованного пользователя
    @Override
    public void updateUserImage(MultipartFile image) throws IOException {
        // Потом сделать через авторизацию
        long userId = 1L;
        UserModel userModel = userRepository.findById(userId).orElse(null);
        //

        String targetFileName= imagesService.makeTargetFileName(image.getOriginalFilename(), userModel.getId(), FileTypeEnum.AVATAR);
        imagesService.saveFileToDisk(image, targetFileName);
        userModel.setImage(targetFileName);
        userRepository.save(userModel);
    }
}
