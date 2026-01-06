package org.example.server.Service;

import jakarta.transaction.Transactional;
import org.example.server.Model.Repository.UserRepository;
import org.example.server.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {
    @Autowired
    private UserRepository userRepository;

    public Page<User> getDriverByBusCode(String busCode, Pageable pageable) {
        return userRepository.findByBusCode(busCode, pageable);
    }

    @Transactional
    public void deleteDriverByPhoneNumber(String phoneNumber) {
        userRepository.deleteByPhoneNumber(phoneNumber);
    }

    @Transactional
    public void updateBlockeDriver(String phoneNumber, int isBlocked){
        userRepository.updateIsBlocked(phoneNumber, isBlocked);
    }
}
