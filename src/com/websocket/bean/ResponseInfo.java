package com.websocket.bean;

public class ResponseInfo {

    private boolean success;

    private boolean failed;

    private String msg;

    public ResponseInfo(boolean success, boolean failed, String msg) {
        this.success = success;
        this.failed = failed;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
