package com.telerik.carpoolingapplication.models.dto;

import javax.validation.constraints.NotNull;

public class CommentDTO {
    @NotNull
    private String message;

    @NotNull
    private int userId;

    public CommentDTO() {
    }

    public CommentDTO(@NotNull String message, @NotNull int userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
