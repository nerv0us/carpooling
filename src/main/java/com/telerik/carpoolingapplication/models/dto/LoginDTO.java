package com.telerik.carpoolingapplication.models.dto;

public class LoginDTO {
    private String password;
    private boolean rememberMe;
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
