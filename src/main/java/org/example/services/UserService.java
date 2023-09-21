package org.example.services;

import org.example.MyAutowired;
import org.example.Service;
import org.example.repositories.UserRepository;

@Service
public class UserService {
    @MyAutowired
    UserRepository userRepository;
    public void performAction() {
        userRepository.saveUser();
    }
}
