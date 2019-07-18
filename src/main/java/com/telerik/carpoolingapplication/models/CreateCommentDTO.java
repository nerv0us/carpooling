package com.telerik.carpoolingapplication.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateCommentDTO {
    @NotNull
    @Size(min = 3, message = "Comment should be at least 3 characters.")
    private String message;

    private UserDTO author;

    public CreateCommentDTO() {
    }

    public CreateCommentDTO(String message, UserDTO author) {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO author) {
        this.author = author;
    }
}
