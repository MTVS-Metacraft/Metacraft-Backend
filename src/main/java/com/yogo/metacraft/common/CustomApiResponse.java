package com.yogo.metacraft.common;

public class CustomApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public CustomApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public CustomApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
