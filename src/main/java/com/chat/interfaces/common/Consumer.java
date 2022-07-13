package com.chat.interfaces.common;

import com.chat.model.other.BroadCastNotification;

public interface Consumer {
    void consumeMessage(BroadCastNotification broadCastNotification);
}
