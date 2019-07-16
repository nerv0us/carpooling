package com.telerik.carpoolingapplication.models;


public class CommentDTO {
    private int id;
    private String message;
    private UserDTO author;

    public CommentDTO() {
    }

    public CommentDTO(int id, String message, UserDTO author) {
        this.id = id;
        this.message = message;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
