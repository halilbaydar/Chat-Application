package com.chat.interfaces.common;

import com.chat.model.other.BroadCastNotification;

public interface Consumer {
    <T> void consumeMessage(BroadCastNotification<T> broadCastNotification);
}
