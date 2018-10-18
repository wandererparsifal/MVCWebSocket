package com.websocket.bean;

public class RegisterResult {

    public static final int CODE_USER_ALREADY_EXISTS = 401;

    public static final int CODE_STORAGE_ERROR = 402;

    private boolean result;

    private int errorCode;

    private User user;

    public RegisterResult(boolean result, int errorCode, User user) {
        this.result = result;
        this.errorCode = errorCode;
        this.user = user;
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
}
