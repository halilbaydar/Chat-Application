package com.chat.model

import com.chat.type.NotificationType
import java.io.Serializable

data class BroadCastNotification<T>(
    val notificationType: NotificationType,
    val payload: T
) : Serializable

