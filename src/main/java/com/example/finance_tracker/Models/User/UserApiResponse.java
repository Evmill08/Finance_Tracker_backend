package com.example.finance_tracker.Models.User;

public class UserApiResponse {
    private boolean success;
    private UserResponse data;
    private String message;

    private UserApiResponse(boolean success, UserResponse data, String message){
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static UserApiResponse success(UserResponse data){
        return new UserApiResponse(true, data, null);
    }

    public static UserApiResponse failure(String message){
        return new UserApiResponse(false, null, message);
    }

    public boolean isSuccess(){
        return this.success;
    }

    public String getMessage(){
        return this.message;
    }

    public UserResponse getData(){
        return this.data;
    }
}
