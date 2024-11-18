package com.yogo.metacraft.common;

public class SimpleApiResponse<T> {
    private T data;

    public SimpleApiResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}