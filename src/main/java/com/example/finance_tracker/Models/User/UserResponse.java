package com.example.finance_tracker.Models.User;

public class UserResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean hasLinkedPlaid;

    public Long getUserId(){
        return this.userId;
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

    public Boolean getHasLinkedPlaid(){
        return this.hasLinkedPlaid;
    }

    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void setHasLinkedPlaid(Boolean hasLinkedPlaid){
        this.hasLinkedPlaid = hasLinkedPlaid;
    }

    public UserResponse(Long userId, String email, String firstName, String lastName, Boolean hasLinkedPlaid){
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hasLinkedPlaid = hasLinkedPlaid;
    }
}
