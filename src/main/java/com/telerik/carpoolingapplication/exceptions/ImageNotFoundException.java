package com.telerik.carpoolingapplication.exceptions;

import java.io.IOException;

public class ImageNotFoundException extends IOException {
    private final String message;

    public ImageNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
