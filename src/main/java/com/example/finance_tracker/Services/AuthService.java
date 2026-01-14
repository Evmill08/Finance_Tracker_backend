package com.example.finance_tracker.Services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.Models.AuthResult;
import com.example.finance_tracker.Models.User;
import com.example.finance_tracker.Repositories.UserRepository;


@Service
public class AuthService {
    private final UserRepository _userRepository;
    private final PasswordEncoder _passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this._userRepository = userRepository;
        this._passwordEncoder = passwordEncoder;
    }

    public AuthResult login(String email, String password){
        User user = _userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if (!_passwordEncoder.matches(password, user.getPasswordHash())){
            throw new RuntimeException("Invalid Password");
        }

        return new AuthResult(user.getId(), user.getEmail());
    }

    public AuthResult signup(String email, String password){
        if (_userRepository.findByEmail(email).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(_passwordEncoder.encode(password));

        User saved = _userRepository.save(user);

        return new AuthResult(saved.getId(), saved.getEmail());
    }
}
