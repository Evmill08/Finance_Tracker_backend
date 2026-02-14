package com.example.finance_tracker.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.Models.User.User;
import com.example.finance_tracker.Models.User.UserApiResponse;
import com.example.finance_tracker.Models.User.UserResponse;
import com.example.finance_tracker.Services.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
    
    private final UserService _userService;

    public UserController(UserService userService){
        this._userService = userService;
    }

    @GetMapping("/user-information")
    public ResponseEntity<UserApiResponse> getUserInformation(@AuthenticationPrincipal User userRequest) {
        User user = _userService.getUserById(userRequest.getId());
        UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.hasLinkedPlaid());
        return ResponseEntity.ok(UserApiResponse.success(response));
    }
}
