package com.telerik.carpoolingapplication.models.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CommentDTO {
    @NotNull
    @Size(min = 3, max = 200, message = "Message should be between 3 and 200 symbols")
    @Lob
    private String message;

    @NotNull
    private int userId;

    public CommentDTO() {
    }

    public CommentDTO(String message, int userId) {
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
