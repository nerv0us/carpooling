package com.telerik.carpoolingapplication.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginDTO {
    @Size(min = 3, max = 50, message = "Password should be between 3 and 50 characters.")
    private String password;

    @NotNull
    private boolean rememberMe;

    @NotNull
    @Size(min = 3, max = 50, message = "Username should be between 3 and 50 characters.")
    private String username;

    public LoginDTO() {
    }

    public LoginDTO(String password, boolean rememberMe, String username) {
        this.password = password;
        this.rememberMe = rememberMe;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
