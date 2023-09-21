package org.example.services;

import org.example.DILibrary.annotation.MyAutowired;
import org.example.repositories.UserRepository;

public class UserService {
    @MyAutowired
    UserRepository userRepository;
    public void performAction() {
        userRepository.saveUser();
    }
}
