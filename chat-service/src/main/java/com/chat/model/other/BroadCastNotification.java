package com.chat.model.other;

import com.chat.constant.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class BroadCastNotification<T> implements Serializable {
    private NotificationType notificationType;
    private T payload;
}
