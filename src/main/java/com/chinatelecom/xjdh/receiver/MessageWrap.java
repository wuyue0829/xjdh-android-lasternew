package com.chinatelecom.xjdh.receiver;

public class MessageWrap {

    public final long message;

    public static MessageWrap getInstance(long message) {
        return new MessageWrap(message);
    }

    private MessageWrap(long message) {
        this.message = message;
    }
}