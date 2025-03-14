package com.example.board.board.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NotificationMessage {
    @JsonProperty("message")
    private String message;

    public NotificationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}