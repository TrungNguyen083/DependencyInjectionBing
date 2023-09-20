package org.example;

public class UserService {
    @MyAutowired
    UserRepository userRepository;

    public void performAction() {
        userRepository.saveUser();
    }
}
