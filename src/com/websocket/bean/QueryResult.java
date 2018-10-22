package com.websocket.bean;

public class QueryResult<T> {

    private int code;

    private T payload;

    public QueryResult(int code, T payload) {
        this.code = code;
        this.payload = payload;
    }

    public int getCode() {
        return code;
    }

    public T getPayload() {
        return payload;
    }
}
