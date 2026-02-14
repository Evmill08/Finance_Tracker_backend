package com.example.finance_tracker.Models.Plaid;

// TODO: All these API responses can be abstracted into one ApiResponse type
public class PlaidApiResponse<T> {
    public boolean success;
    public T data;
    public String message;

    public PlaidApiResponse(boolean success, T data, String message){
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> PlaidApiResponse<T> success(T data){
        return new PlaidApiResponse<>(true, data, null);
    }
    
    public static <T> PlaidApiResponse<T> failure(String message){
        return new PlaidApiResponse<>(false, null, message);
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
