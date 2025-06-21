package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService {

    // Обновление пароля
    @Override
    public void setPassword(NewPassword newPassword) {

    }

    // Получение информации об авторизованном пользователе
    @Override
    public User getUser() {
        return new User();
    }

    // Обновление информации об авторизованном пользователе
    @Override
    public UpdateUser updateUser(UpdateUser user) {
        return user;
    }

    // Обновление аватара авторизованного пользователя
    @Override
    public void updateUserImage(MultipartFile image) {

    }
}
