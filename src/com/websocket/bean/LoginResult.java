package com.websocket.bean;

public class LoginResult {

    private boolean result;

    private int errorCode;

    private User user;

    private String token;

    public LoginResult(boolean result, int errorCode, User user, String token) {
        this.result = result;
        this.errorCode = errorCode;
        this.user = user;
        this.token = token;
    }

    public boolean isSuccess() {
        return result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}
