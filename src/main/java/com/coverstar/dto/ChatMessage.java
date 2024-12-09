package com.coverstar.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage {
    private String content;
    private String sender;
    private LocalDateTime timeStamp = LocalDateTime.now();

    public enum MessageType {LEAVE, CHAT, JOIN}

    private MessageType type;
}
