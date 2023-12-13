package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final int code;
    private final String error;

    public ErrorResponse(int code, String error) {
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
