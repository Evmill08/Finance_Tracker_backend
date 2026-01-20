package com.example.finance_tracker.Models.User;

public class UserResponse {
    private Long Id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean hasLinkedPlaid;

    public Long getUserId(){
        return this.Id;
    }

    public String getEmail(){
        return this.email;
    }  

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public Boolean hasLinkedPlaid(){
        return this.hasLinkedPlaid;
    }    

    public UserResponse(Long Id, String email, String firstName, String lastName, Boolean hasLinkedPlaid){
        this.Id = Id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hasLinkedPlaid = hasLinkedPlaid;
    }
}
