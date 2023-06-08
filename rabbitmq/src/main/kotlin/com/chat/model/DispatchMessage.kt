package com.chat.model

import com.chat.type.MessageType
import java.io.Serializable

data class DispatchMessage<T>(
    val messageType: MessageType,
    val payload: T
) : Serializable

