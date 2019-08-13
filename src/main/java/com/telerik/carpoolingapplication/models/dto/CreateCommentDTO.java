package com.telerik.carpoolingapplication.models.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateCommentDTO {
    @NotNull
    @Size(min = 3, max = 200, message = "Message should be between 3 and 200 symbols")
    @Lob
    private String message;

    @NotNull
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
