package com.example.finance_tracker.Models.User;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    @Column(nullable=false)
    private boolean emailVerified;

    @Column(nullable=false)
    private String firstName;

    @Column(nullable=false)
    private String lastName;

    @Column(nullable=false)
    private boolean hasLinkedPlaid;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    private List<String> roles;

    public Long getId()
    {
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

    public boolean isEmailVerified(){
        return this.emailVerified;
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

    public List<String> getRoles(){
        return this.roles;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String hashedPassword){
        this.passwordHash = hashedPassword;
    }

    public void setIsEmailVerified(boolean emailVerified){
        this.emailVerified = emailVerified;
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

    public void setRoles(List<String> roles){
        this.roles = roles;
    }

    public User(String email, String passwordHash, Boolean isEmailVerified, String firstName, String lastName, Boolean hasLinkedPlaid, List<String> roles){
        this.email = email;
        this.passwordHash = passwordHash;
        this.emailVerified = isEmailVerified;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hasLinkedPlaid = hasLinkedPlaid;
        this.roles = roles;
    }

    public User() {}
}
