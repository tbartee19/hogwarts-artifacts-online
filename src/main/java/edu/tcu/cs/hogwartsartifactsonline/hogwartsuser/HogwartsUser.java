package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;

import java.io.Serializable;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;


@Entity
public class HogwartsUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @NotEmpty(message = "Username is required.")
    private String username;

    @NotEmpty(message = "Password is required.")
    private String password;

    private boolean enabled;

    @NotEmpty(message = "Roles are required.")
    private String roles;

     // Getters and Setters
     public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
