package com.example.finance_tracker.Models.Auth;

public class AuthApiResponse<T> {
    private boolean success;
    private T data;
    private String message;

    private AuthApiResponse(boolean success, T data, String message){
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> AuthApiResponse<T> success(T data){
        return new AuthApiResponse<>(true, data, null);
    }

    public static <T> AuthApiResponse<T> failure(String message){
        return new AuthApiResponse<>(false, null, message);
    }

    public boolean isSuccess(){
        return this.success;
    }    
    public String getMessage(){
        return this.message;
    }
    public T getData(){
        return this.data;
    }

}
