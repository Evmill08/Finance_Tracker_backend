package com.example.finance_tracker.Services;

import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository _userRepository;

    public UserService(UserRepository userRepository){
        this._userRepository = userRepository;
    }

    public User getUserById(Long userId){
        User user = _userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found")); // Think about this error

        return user;
    }
}
