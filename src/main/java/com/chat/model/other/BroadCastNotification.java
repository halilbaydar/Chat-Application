package com.chat.model.other;

import com.chat.constant.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BroadCastNotification<T> {
    private NotificationType notificationType;
    private T payload;
}
