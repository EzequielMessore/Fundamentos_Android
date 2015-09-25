package com.br.fundamentosandroid.models.service;

import com.br.fundamentosandroid.R;
import com.br.fundamentosandroid.exceptions.UserAlreadyRegistered;
import com.br.fundamentosandroid.models.entities.User;
import com.br.fundamentosandroid.models.persistence.UserRepository;
import com.br.fundamentosandroid.util.AppUtil;

import java.util.List;

public class UserBusinessService {

    private UserBusinessService() {
        super();
    }

    public static boolean isUser(User user) {
        return UserRepository.isUser(user.getName(), user.getPassword());
    }

    public static void save(User user) throws UserAlreadyRegistered {
        if (!isUser(user)) {
            UserRepository.saveUser(user);
        } else {
            throw new UserAlreadyRegistered(AppUtil.CONTEXT_APPLICATION.getResources().getString(R.string.msg_user_already_registered));
        }
    }

    public static List<User> getAll() {
        return UserRepository.getAll();
    }

    public static User getUserByName(String name) {
        return UserRepository.getUserByName(name);
    }

    public static User getUserById(int id) {
        return UserRepository.getUserById(id);
    }
}
