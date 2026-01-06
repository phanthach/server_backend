package org.example.server.Service;



import org.example.server.Model.User;
import org.example.server.Model.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public int checkLogin(String phoneNumber, String password) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Kiểm tra mật khẩu
            if(user.getPassword().equals(password)) {
                return 1;
            }
            else {
                return 2;
            }
        }
        return 0;
    }
    public User getUserByPhoneNumber(String phoneNumber) {
        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        return userOptional.orElse(null);
    }
    public User addUser(User user) {
        user.setCreatedAt(String.valueOf(LocalDateTime.now()));
        return userRepository.save(user);
    }

    public boolean isPhoneExists(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }
}

