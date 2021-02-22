package com.example.domain.api;

public class Message {
    private String text;

    public Message() {
        // required for (de)serialization
    }

    public Message(String message) {
        this.text = message;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
