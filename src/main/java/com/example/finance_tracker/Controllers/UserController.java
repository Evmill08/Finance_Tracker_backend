package com.example.finance_tracker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.User.UserRequest;
import com.example.finance_tracker.Models.User.UserResponse;
import com.example.finance_tracker.Services.JwtService;
import com.example.finance_tracker.Services.UserService;



@RestController
@RequestMapping("/user")
public class UserController {
    
    private final JwtService _jwtService;
    private final UserService _userService;

    public UserController(JwtService jwtService, UserService userService){
        this._jwtService = jwtService;
        this._userService = userService;
    }

    @PostMapping("/user-information")
    public ResponseEntity<UserResponse> getUserInformation(@RequestBody UserRequest request) {
        Long userId = _jwtService.getUserIdFromToken(request.getJwtToken());
        UserResponse response = _userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }
}
