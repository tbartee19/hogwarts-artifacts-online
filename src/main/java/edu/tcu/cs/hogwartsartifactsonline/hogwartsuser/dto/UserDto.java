package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Integer id,

                    @NotEmpty(message = "Username is required.")
                    String username,

                    boolean enabled, 

                    @NotEmpty(message = "Roles are required.")
                    String roles
) {


    // Getters
    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getRoles() {
        return roles;
    }


}
