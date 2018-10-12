package com.websocket.bean;

public class Message {

    private String fromWho;

    private String text;

    private String date;

    public Message() {
    }

    public Message(String fromWho, String text, String date) {
        this.fromWho = fromWho;
        this.text = text;
        this.date = date;
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
