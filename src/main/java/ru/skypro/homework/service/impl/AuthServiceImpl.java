package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.UserModel;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder) {
        this.manager = manager;
        this.encoder = passwordEncoder;
    }

    /**
     * Авториация пользователя
     * @param userName String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    /**
     * Регистрация нового пользователя
     * @param register Register
     * @return boolean
     */
    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }

        UserModel userModel = new UserModel();
        userModel.setPhone(register.getPhone());
        userModel.setEmail(register.getUsername());
        userModel.setPassword(encoder.encode(register.getPassword()));
        userModel.setRole(register.getRole().toString());
        userModel.setFirstName(register.getFirstName());
        userModel.setLastName(register.getLastName());
        manager.createUser(userModel);
        return true;
    }
}
